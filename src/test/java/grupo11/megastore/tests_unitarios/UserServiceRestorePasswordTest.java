package grupo11.megastore.tests_unitarios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.APIException;
import grupo11.megastore.users.model.User;
import grupo11.megastore.users.model.repository.UserRepository;
import grupo11.megastore.users.services.UserService;

public class UserServiceRestorePasswordTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private String emailValido;
    private String tokenValido;
    private User userActivo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        emailValido = "user@example.com";
        tokenValido = UUID.randomUUID().toString();

        userActivo = new User();
        userActivo.setId(1L);
        userActivo.setEmail(emailValido);
        userActivo.setStatus(EntityStatus.ACTIVE);
        userActivo.setPassword("oldPasswordHash");

        when(userRepository.findByEmailAndStatus(emailValido, EntityStatus.ACTIVE))
                .thenReturn(Optional.of(userActivo));

        userService.sendResetToken(emailValido);

        try {
            var field = UserService.class.getDeclaredField("resetTokens");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            var resetTokens = (java.util.Map<String, String>) field.get(userService);
            resetTokens.put(emailValido, tokenValido);
        } catch (Exception e) {
            fail("No se pudo inyectar el token de prueba.");
        }
    }

    // Tests 1.5.3
    @Test
    void testRestaurarConTokenValido() {
        String nuevaPassword = "PasswordNueva123";
        when(passwordEncoder.encode(nuevaPassword)).thenReturn("encodedNewPassword");

        userService.restorePassword(emailValido, tokenValido, nuevaPassword);

        verify(userRepository, times(1)).save(userActivo);
        assertEquals("encodedNewPassword", userActivo.getPassword(),
                "La contraseña del usuario debería haberse actualizado");
    }

    @Test
    void testRestaurarConTokenInvalido() {
        String emailInvalido = "user2@example.com";
        User otroUsuario = new User();
        otroUsuario.setId(2L);
        otroUsuario.setEmail(emailInvalido);
        otroUsuario.setStatus(EntityStatus.ACTIVE);
        otroUsuario.setPassword("otroOldPassword");

        when(userRepository.findByEmailAndStatus(emailInvalido, EntityStatus.ACTIVE))
                .thenReturn(Optional.of(otroUsuario));

        userService.sendResetToken(emailInvalido);

        String tokenInvalido = UUID.randomUUID().toString();
        String nuevaPassword = "PasswordInvalida123";

        APIException thrown = assertThrows(APIException.class, () -> {
            userService.restorePassword(emailInvalido, tokenInvalido, nuevaPassword);
        }, "Se esperaba una APIException por token inválido");

        assertEquals("Token de restablecimiento inválido o expirado.", thrown.getMessage(),
                "El mensaje de error debería indicar que el token es inválido o expirado");
        assertEquals("otroOldPassword", otroUsuario.getPassword(),
                "La contraseña del usuario no debería cambiar");
    }
}
