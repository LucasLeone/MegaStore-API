package grupo11.megastore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.APIException;
import grupo11.megastore.products.dto.IVariantMapper;
import grupo11.megastore.products.dto.variant.UpdateVariantDTO;
import grupo11.megastore.products.dto.variant.VariantDTO;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.products.model.Variant;
import grupo11.megastore.products.model.repository.ProductRepository;
import grupo11.megastore.products.model.repository.VariantRepository;
import grupo11.megastore.products.service.VariantService;

public class VariantServiceStockUpdateTest {

    @Mock
    private VariantRepository variantRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private IVariantMapper variantMapper;

    @InjectMocks
    private VariantService variantService;

    @BeforeEach
    void setUpService() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests 1.5.1
    @Test
    void testIncrementarStockVarianteConValorValido() {
        Variant variant = new Variant();
        variant.setId(1L);
        variant.setStatus(EntityStatus.ACTIVE);
        variant.setStock(10);
        Product product = new Product();
        variant.setProduct(product);

        when(variantRepository.findByIdAndStatus(1L, EntityStatus.ACTIVE))
                .thenReturn(Optional.of(variant));

        when(productRepository.findByIdAndStatus(anyLong(), eq(EntityStatus.ACTIVE)))
                .thenReturn(Optional.of(product));

        UpdateVariantDTO updateDTO = new UpdateVariantDTO();
        updateDTO.setStock(15);

        Variant updatedVariant = new Variant();
        updatedVariant.setId(1L);
        updatedVariant.setStatus(EntityStatus.ACTIVE);
        updatedVariant.setStock(15);
        updatedVariant.setProduct(product);

        when(variantRepository.save(any(Variant.class))).thenReturn(updatedVariant);

        VariantDTO updatedVariantDTO = new VariantDTO();
        updatedVariantDTO.setId(1L);
        updatedVariantDTO.setStock(15);
        when(variantMapper.variantToVariantDTO(any(Variant.class))).thenReturn(updatedVariantDTO);

        VariantDTO result = variantService.updateVariant(1L, updateDTO);

        assertEquals(15, result.getStock(), "La variante debería tener el stock actualizado a 15");
    }

    @Test
    void testDecrementarStockVarianteConValorValido() {
        Variant variant = new Variant();
        variant.setId(1L);
        variant.setStatus(EntityStatus.ACTIVE);
        variant.setStock(10);
        Product product = new Product();
        variant.setProduct(product);

        when(variantRepository.findByIdAndStatus(1L, EntityStatus.ACTIVE))
                .thenReturn(Optional.of(variant));

        when(productRepository.findByIdAndStatus(anyLong(), eq(EntityStatus.ACTIVE)))
                .thenReturn(Optional.of(product));

        UpdateVariantDTO updateDTO = new UpdateVariantDTO();
        updateDTO.setStock(5);

        Variant updatedVariant = new Variant();
        updatedVariant.setId(1L);
        updatedVariant.setStatus(EntityStatus.ACTIVE);
        updatedVariant.setStock(5);
        updatedVariant.setProduct(product);

        when(variantRepository.save(any(Variant.class))).thenReturn(updatedVariant);

        VariantDTO updatedVariantDTO = new VariantDTO();
        updatedVariantDTO.setId(1L);
        updatedVariantDTO.setStock(5);
        when(variantMapper.variantToVariantDTO(any(Variant.class))).thenReturn(updatedVariantDTO);

        VariantDTO result = variantService.updateVariant(1L, updateDTO);

        assertEquals(5, result.getStock(), "La variante debería tener el stock actualizado a 5");
    }

    @Test
    void testIntentarActualizarStockVarianteConValorNegativo() {
        Variant variant = new Variant();
        variant.setId(1L);
        variant.setStatus(EntityStatus.ACTIVE);
        variant.setStock(5);
        Product product = new Product();
        variant.setProduct(product);

        when(variantRepository.findByIdAndStatus(1L, EntityStatus.ACTIVE))
                .thenReturn(Optional.of(variant));

        UpdateVariantDTO updateDTO = new UpdateVariantDTO();
        updateDTO.setStock(-3);

        APIException thrown = assertThrows(APIException.class, () -> {
            variantService.updateVariant(1L, updateDTO);
        }, "Se esperaba una APIException al intentar actualizar el stock a un valor negativo");

        assertEquals("El stock no puede ser negativo", thrown.getMessage(),
                "El mensaje de la excepción debería ser 'El stock no puede ser negativo'");
    }
}
