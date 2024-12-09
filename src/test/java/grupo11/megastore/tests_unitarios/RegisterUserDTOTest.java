package grupo11.megastore.tests_unitarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import grupo11.megastore.users.dto.user.RegisterUserDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class RegisterUserDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Tests 1.1.2
    @Test
    void testNombreVacio() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setFirstName(""); // Nombre vacío, no cumple con min=2

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si el nombre está vacío");

        ConstraintViolation<RegisterUserDTO> violation = violations.iterator().next();
        assertEquals("El nombre debe tener entre 2 y 20 caracteres", violation.getMessage());
    }

    @Test
    void testNombreDemasiadoLargo() {
        RegisterUserDTO dto = new RegisterUserDTO();
        // Crear un nombre de 21 caracteres para violar max=20
        dto.setFirstName("abcdefghijklmnopqrsua");

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si el nombre excede los 20 caracteres");

        ConstraintViolation<RegisterUserDTO> violation = violations.iterator().next();
        assertEquals("El nombre debe tener entre 2 y 20 caracteres", violation.getMessage());
    }

    @Test
    void testNombreDemasiadoCorto() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setFirstName("A"); // Solo 1 caracter, min es 2

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si el nombre es demasiado corto");

        ConstraintViolation<RegisterUserDTO> violation = violations.iterator().next();
        assertEquals("El nombre debe tener entre 2 y 20 caracteres", violation.getMessage());
    }

    @Test
    void testNombreValido() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setFirstName("Maria"); // 5 caracteres, dentro del rango 2-20

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con un nombre válido");
    }

    // Tests 1.1.3
    @Test
    void testContrasenaVacia() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setPassword(""); // Contraseña vacía, no cumple con min=8

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si la contraseña está vacía");

        ConstraintViolation<RegisterUserDTO> violation = violations.iterator().next();
        assertEquals("La contraseña debe tener entre 8 y 20 caracteres", violation.getMessage());
    }

    @Test
    void testContrasenaDemasiadoCorta() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setPassword("1234567"); // 7 caracteres, min=8

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones con contraseña demasiado corta");

        ConstraintViolation<RegisterUserDTO> violation = violations.iterator().next();
        assertEquals("La contraseña debe tener entre 8 y 20 caracteres", violation.getMessage());
    }

    @Test
    void testContrasenaDemasiadoLarga() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setPassword("123456789012345678901"); // 21 caracteres

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones con contraseña demasiado larga");

        ConstraintViolation<RegisterUserDTO> violation = violations.iterator().next();
        assertEquals("La contraseña debe tener entre 8 y 20 caracteres", violation.getMessage());
    }

    @Test
    void testContrasenaValida() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setPassword("12345678"); // 8 caracteres, límite inferior válido

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con una contraseña válida");
    }

    // Tests 1.3.2
    @Test
    void testEmailInvalido() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setEmail("usuario@invalid"); // Email sin dominio

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones con un email inválido");

        ConstraintViolation<RegisterUserDTO> violation = violations.iterator().next();
        assertEquals("El email debe ser válido", violation.getMessage());
    }

    @Test
    void testContrasenaCorta() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setEmail("usuario@example.com");
        dto.setPassword("123"); // Contraseña de 3 caracteres, no cumple con min=8

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones con una contraseña corta");

        ConstraintViolation<RegisterUserDTO> violation = violations.iterator().next();
        assertEquals("La contraseña debe tener entre 8 y 20 caracteres", violation.getMessage());
    }

    @Test
    void testEmailInvalidoYContrasenaCorta() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setEmail("usuario@invalid"); // Email inválido
        dto.setPassword("123"); // Contraseña corta

        Set<ConstraintViolation<RegisterUserDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones con email inválido y contraseña corta");

        boolean hasEmailViolation = violations.stream()
                .anyMatch(v -> "El email debe ser válido".equals(v.getMessage()));
        boolean hasPasswordViolation = violations.stream()
                .anyMatch(v -> "La contraseña debe tener entre 8 y 20 caracteres".equals(v.getMessage()));

        assertTrue(hasEmailViolation, "Debe haber una violación con mensaje 'El email debe ser válido'");
        assertTrue(hasPasswordViolation,
                "Debe haber una violación con mensaje 'La contraseña debe tener entre 8 y 20 caracteres'");
    }
}
