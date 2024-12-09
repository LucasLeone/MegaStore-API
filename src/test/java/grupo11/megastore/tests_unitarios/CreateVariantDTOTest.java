package grupo11.megastore.tests_unitarios;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.APIException;
import grupo11.megastore.products.dto.IVariantMapper;
import grupo11.megastore.products.dto.variant.CreateVariantDTO;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.products.model.Variant;
import grupo11.megastore.products.model.repository.VariantRepository;
import grupo11.megastore.products.service.VariantService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)
public class CreateVariantDTOTest {

    private Validator validator;

    @Mock
    private VariantRepository variantRepository;

    @Mock
    private IVariantMapper variantMapper;

    @InjectMocks
    private VariantService variantService;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Tests 1.1.5
    @Test
    void testStockNulo() {

        CreateVariantDTO dto = new CreateVariantDTO();
        dto.setProductId(1L);
        dto.setColor("Azul");
        dto.setSize("M");
        dto.setStock(null);

        Set<ConstraintViolation<CreateVariantDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber una violación si el stock es nulo");

        boolean foundExpectedMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El stock es obligatorio"));
        assertTrue(foundExpectedMessage, "Debería haberse encontrado el mensaje 'El stock es obligatorio'");
    }

    @Test
    void testStockNegativo() {
        CreateVariantDTO dto = new CreateVariantDTO();
        dto.setProductId(1L);
        dto.setColor("Azul");
        dto.setSize("M");
        dto.setStock(-1);

        Set<ConstraintViolation<CreateVariantDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber una violación si el stock es negativo");

        boolean foundExpectedMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El stock no puede ser negativo"));
        assertTrue(foundExpectedMessage, "Debería haberse encontrado el mensaje 'El stock no puede ser negativo'");
    }

    @Test
    void testStockIgualACero() {
        CreateVariantDTO dto = new CreateVariantDTO();
        dto.setProductId(1L);
        dto.setColor("Azul");
        dto.setSize("M");
        dto.setStock(0);

        Set<ConstraintViolation<CreateVariantDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con stock = 0");
    }

    @Test
    void testStockValido() {
        CreateVariantDTO dto = new CreateVariantDTO();
        dto.setProductId(1L);
        dto.setColor("Azul");
        dto.setSize("M");
        dto.setStock(10);

        Set<ConstraintViolation<CreateVariantDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con stock = 10");
    }

    // Tests 1.2.5
    @Test
    void testColorVacio() {
        CreateVariantDTO dto = new CreateVariantDTO();
        dto.setProductId(1L);
        dto.setSize("1L");
        dto.setStock(10);
        dto.setColor("");

        Set<ConstraintViolation<CreateVariantDTO>> violations = validator.validate(dto);

        assertTrue(!violations.isEmpty(), "Debería haber al menos una violación");
        boolean foundExpectedMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El color es obligatorio"));

        assertTrue(foundExpectedMessage, "Debería haberse encontrado el mensaje 'El color es obligatorio'");
    }

    @Test
    void testColorDemasiadoLargo() {
        CreateVariantDTO dto = new CreateVariantDTO();
        dto.setProductId(1L);
        dto.setSize("1L");
        dto.setStock(10);
        dto.setColor("ColorExtremadamenteLargoParaPrueba");

        Set<ConstraintViolation<CreateVariantDTO>> violations = validator.validate(dto);

        assertTrue(!violations.isEmpty(), "Debería haber una violación si el color excede los 32 caracteres");
        boolean foundExpectedMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El color no puede exceder los 32 caracteres"));

        assertTrue(foundExpectedMessage,
                "Debería haberse encontrado el mensaje 'El color no puede exceder los 32 caracteres'");
    }

    @Test
    void testColorLongitudMinimaPermitida() {
        CreateVariantDTO dto = new CreateVariantDTO();
        dto.setProductId(1L);
        dto.setSize("1L");
        dto.setStock(10);
        dto.setColor("A");

        Set<ConstraintViolation<CreateVariantDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "No debería haber violaciones con un color de 1 carácter");
    }

    @Test
    void testColorLongitudIntermediaValida() {
        CreateVariantDTO dto = new CreateVariantDTO();
        dto.setProductId(1L);
        dto.setSize("1L");
        dto.setStock(10);
        dto.setColor("ColorMedioValido");

        Set<ConstraintViolation<CreateVariantDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "No debería haber violaciones con un color de 15 caracteres");
    }

    // Tests 1.3.5
    @Test
    void testColorVacioYStockVacio() {
        CreateVariantDTO dto = new CreateVariantDTO();
        dto.setProductId(1L);
        dto.setSize("1L");
        dto.setColor("");
        dto.setStock(null);

        Set<ConstraintViolation<CreateVariantDTO>> violations = validator.validate(dto);

        assertTrue(!violations.isEmpty(), "Debería haber al menos una violación");
        boolean foundExpectedMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El color es obligatorio"));

        assertTrue(foundExpectedMessage, "Debería haberse encontrado el mensaje 'El color es obligatorio'");
    }

    void testColorValidoYStockValido() {
        CreateVariantDTO dto = new CreateVariantDTO();
        dto.setProductId(1L);
        dto.setSize("1L");
        dto.setColor("Azul");
        dto.setStock(10);

        Set<ConstraintViolation<CreateVariantDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "No debería haber violaciones con color y stock válidos");
    }

    // Tests 1.5.5
    @Test
    void testColorYTamanoDuplicado() {
        Product existingProduct = new Product();
        existingProduct.setId(1L);

        Variant existingVariant = new Variant();
        existingVariant.setColor("Rojo");
        existingVariant.setSize("M");
        existingVariant.setProduct(existingProduct);

        when(variantRepository.findByProductIdAndColorAndSizeAndStatus(existingVariant.getProduct().getId(),
                existingVariant.getColor(), existingVariant.getSize(), EntityStatus.ACTIVE))
                .thenReturn(Optional.of(existingVariant));

        CreateVariantDTO dto = new CreateVariantDTO();
        dto.setProductId(1L);
        dto.setColor("Rojo");
        dto.setSize("M");
        
        Exception exception = assertThrows(APIException.class, () -> {
            variantService.createVariant(dto);
        });

        String expectedMessage = "La variante ya existe";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Debería ver un mensaje de error que dice 'Ya existe una variante con el mismo color y tamaño para el producto'");

        verify(variantRepository, never()).save(existingVariant);
    }
}
