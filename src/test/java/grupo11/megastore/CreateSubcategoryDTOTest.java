package grupo11.megastore;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import grupo11.megastore.products.dto.subcategory.CreateSubcategoryDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CreateSubcategoryDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Tests 1.3.4
    @Test
    void testNombreDemasiadoCortoYCategoriaNula() {
        CreateSubcategoryDTO dto = new CreateSubcategoryDTO();
        dto.setName("a");
        dto.setCategoryId(null);

        Set<ConstraintViolation<CreateSubcategoryDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si el nombre tiene menos de 2 caracteres y la categoría es nula");

        boolean hasSizeViolation = violations.stream()
                .anyMatch(v -> "El nombre debe tener entre 2 y 32 caracteres".equals(v.getMessage()));
        assertTrue(hasSizeViolation, "Debe haber una violación con mensaje 'El nombre debe tener entre 2 y 32 caracteres'");
    }

    @Test
    void testCategoriaNula() {
        CreateSubcategoryDTO dto = new CreateSubcategoryDTO();
        dto.setCategoryId(null);

        Set<ConstraintViolation<CreateSubcategoryDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si la categoría es nula");

        boolean hasNotNullViolation = violations.stream()
                .anyMatch(v -> "La categoría es requerida".equals(v.getMessage()));
        assertTrue(hasNotNullViolation, "Debe haber una violación con mensaje 'La categoría es requerida'");
    }

    @Test
    void testNombreDemasiadoCortoYCategoriaInexistente() {
        CreateSubcategoryDTO dto = new CreateSubcategoryDTO();
        dto.setName("a");
        dto.setCategoryId(1L);

        Set<ConstraintViolation<CreateSubcategoryDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si el nombre tiene menos de 2 caracteres y la categoría no existe");

        boolean hasSizeViolation = violations.stream()
                .anyMatch(v -> "El nombre debe tener entre 2 y 32 caracteres".equals(v.getMessage()));
        assertTrue(hasSizeViolation, "Debe haber una violación con mensaje 'El nombre debe tener entre 2 y 32 caracteres'");
    }

    @Test
    void testNombreValidoYCategoriaValida() {
        CreateSubcategoryDTO dto = new CreateSubcategoryDTO();
        dto.setName("ab");
        dto.setCategoryId(1L);

        Set<ConstraintViolation<CreateSubcategoryDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones si el nombre tiene 2 caracteres y la categoría existe");
    }
}
