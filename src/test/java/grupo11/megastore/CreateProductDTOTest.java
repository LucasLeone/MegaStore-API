package grupo11.megastore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import grupo11.megastore.products.dto.product.CreateProductDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CreateProductDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNombreVacio() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("");
        dto.setPrice(10.0);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber al menos una violación");

        boolean foundExpectedMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El nombre es obligatorio"));

        assertTrue(foundExpectedMessage, "Debería haberse encontrado el mensaje 'El nombre es obligatorio'");
    }

    @Test
    void testNombreDemasiadoCorto() {
        // Escenario: Nombre con 1 caracter
        // Resultado esperado: "El nombre debe tener entre 2 y 32 caracteres"

        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("A");
        dto.setPrice(10.0);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber una violación si el nombre tiene menos de 2 caracteres");

        ConstraintViolation<CreateProductDTO> violation = violations.iterator().next();
        assertEquals("El nombre debe tener entre 2 y 32 caracteres", violation.getMessage());
    }

    @Test
    void testNombreDemasiadoLargo() {
        // Escenario: Nombre con 33 caracteres
        // Resultado esperado: "El nombre debe tener entre 2 y 32 caracteres"

        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("abcdefghijklmnopqrstuvwx123456789abc"); // 33 caracteres
        dto.setPrice(10.0);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber una violación si el nombre excede los 32 caracteres");

        ConstraintViolation<CreateProductDTO> violation = violations.iterator().next();
        assertEquals("El nombre debe tener entre 2 y 32 caracteres", violation.getMessage());
    }

    @Test
    void testNombreValido() {
        // Escenario: Nombre con 10 caracteres, dentro del rango 2-32
        // Resultado esperado: Sin violaciones

        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("Producto10"); // 10 caracteres
        dto.setPrice(10.0);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con un nombre válido");
    }
}
