package grupo11.megastore;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import grupo11.megastore.products.dto.variant.CreateVariantDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CreateVariantDTOTest {

    private Validator validator;

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
}
