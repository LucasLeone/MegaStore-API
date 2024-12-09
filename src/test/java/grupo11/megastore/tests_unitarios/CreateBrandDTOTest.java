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
import grupo11.megastore.products.dto.IBrandMapper;
import grupo11.megastore.products.dto.brand.CreateBrandDTO;
import grupo11.megastore.products.model.Brand;
import grupo11.megastore.products.model.repository.BrandRepository;
import grupo11.megastore.products.service.BrandService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)
public class CreateBrandDTOTest {

    private Validator validator;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private IBrandMapper brandMapper;

    @InjectMocks
    private BrandService brandService;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Tests 1.1.1
    @Test
    void testNombreVacio() {
        CreateBrandDTO dto = new CreateBrandDTO();
        dto.setName(""); // Vacío

        Set<ConstraintViolation<CreateBrandDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si el nombre está vacío");

        boolean hasNotEmptyViolation = violations.stream()
                .anyMatch(v -> "El nombre es requerido".equals(v.getMessage()));
        assertTrue(hasNotEmptyViolation, "Debe haber una violación con mensaje 'El nombre es requerido'");
    }

    @Test
    void testNombreDemasiadoLargo() {
        CreateBrandDTO dto = new CreateBrandDTO();
        dto.setName("abcdefghijklmnopqrstuvwx123456789abc"); // 33 caracteres

        Set<ConstraintViolation<CreateBrandDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si el nombre excede los 32 caracteres");

        boolean hasSizeViolation = violations.stream()
                .anyMatch(v -> "El nombre debe tener entre 3 y 32 caracteres".equals(v.getMessage()));
        assertTrue(hasSizeViolation,
                "Debe haber una violación con mensaje 'El nombre debe tener entre 3 y 32 caracteres'");
    }

    @Test
    void testNombreDemasiadoCorto() {
        CreateBrandDTO dto = new CreateBrandDTO();
        dto.setName("Ab"); // 2 caracteres

        Set<ConstraintViolation<CreateBrandDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si el nombre tiene menos de 3 caracteres");

        boolean hasSizeViolation = violations.stream()
                .anyMatch(v -> "El nombre debe tener entre 3 y 32 caracteres".equals(v.getMessage()));
        assertTrue(hasSizeViolation,
                "Debe haber una violación con mensaje 'El nombre debe tener entre 3 y 32 caracteres'");
    }

    // Tests 1.2.1
    @Test
    void testNombreValido() {
        CreateBrandDTO dto = new CreateBrandDTO();
        dto.setName("Nike"); // 4 caracteres

        Set<ConstraintViolation<CreateBrandDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones si el nombre es válido");
    }

    // Tests 1.5.4
    @Test
    void testCrearMarcaDuplicada() {
        Brand existingBrand = new Brand();
        existingBrand.setId(1L);
        existingBrand.setName("Nike");
        existingBrand.setStatus(EntityStatus.ACTIVE);

        when(brandRepository.findByNameIgnoreCaseAndStatus("Nike", EntityStatus.ACTIVE))
                .thenReturn(Optional.of(existingBrand));

        CreateBrandDTO dtoDuplicada = new CreateBrandDTO();
        dtoDuplicada.setName("Nike");
        dtoDuplicada.setDescription("Otra descripción");

        Exception exception = assertThrows(APIException.class, () -> {
            brandService.createBrand(dtoDuplicada);
        });

        String expectedMessage = "La marca ya existe";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage),
                "Debería ver un mensaje de error que dice 'La marca ya existe'");

        verify(brandRepository, never()).save(existingBrand);
    }
}
