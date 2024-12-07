package grupo11.megastore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import grupo11.megastore.security.JWTUtil;
import grupo11.megastore.users.dto.user.LoginCredentials;
import grupo11.megastore.users.dto.user.UserDTO;
import grupo11.megastore.users.interfaces.IUserService;
import grupo11.megastore.users.services.AuthService;
import grupo11.megastore.exception.APIException;

@ExtendWith(MockitoExtension.class)
public class AuthServiceUnitTest {

    @Mock
    private IUserService userService;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private UserDTO mockUserDTO;

    @BeforeEach
    void setUp() {
        mockUserDTO = new UserDTO();
        mockUserDTO.setEmail("pepe@mail.com");
    }

    // Tests 1.2.2
    @Test
    void testContrasenaIncorrecta() {
        LoginCredentials credentials = new LoginCredentials();
        credentials.setEmail("pepe@mail.com");
        credentials.setPassword("admin12345");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        APIException thrown = assertThrows(APIException.class, () -> {
            authService.login(credentials);
        });

        assertEquals("Credenciales inválidas", thrown.getMessage());

        verify(jwtUtil, never()).generateToken(anyString());
        verify(userService, never()).getUserByEmail(anyString());
    }

    // Tests 1.2.3
    @Test
    void testCorreoIncorrecto() {
        LoginCredentials credentials = new LoginCredentials();
        credentials.setEmail("X@mail.com");
        credentials.setPassword("Contraseña123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        APIException thrown = assertThrows(APIException.class, () -> {
            authService.login(credentials);
        });

        assertEquals("Credenciales inválidas", thrown.getMessage());

        verify(jwtUtil, never()).generateToken(anyString());
        verify(userService, never()).getUserByEmail(anyString());
    }
}
