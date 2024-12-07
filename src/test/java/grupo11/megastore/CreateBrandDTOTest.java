package grupo11.megastore;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import grupo11.megastore.products.dto.brand.CreateBrandDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CreateBrandDTOTest {

    private Validator validator;

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
}
