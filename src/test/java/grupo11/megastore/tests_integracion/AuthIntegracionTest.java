package grupo11.megastore.tests_integracion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import grupo11.megastore.config.TestConfig;
import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.users.model.User;
import grupo11.megastore.users.model.repository.UserRepository;
import jakarta.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import grupo11.megastore.users.dto.user.LoginCredentials;
import grupo11.megastore.users.dto.user.RegisterUserDTO;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
public class AuthIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        User user = new User();
        user.setEmail("testuser@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setPhoneNumber("21312312321");
        user.setStatus(EntityStatus.ACTIVE);
        userRepository.save(user);
    }

    // Tests 2.2.1
    @Test
    void testLoginCorreoFormatoValido() throws Exception {
        LoginCredentials credentials = new LoginCredentials();
        credentials.setEmail("testuser@example.com");
        credentials.setPassword("password");

        String jsonRequest = objectMapper.writeValueAsString(credentials);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.jwt-token").isNotEmpty())
                .andExpect(jsonPath("$.user.email").value("testuser@example.com"));
    }

    @Test
    void testLoginCorreoFormatoInvalido() throws Exception {
        LoginCredentials credentials = new LoginCredentials();
        credentials.setEmail("invalid-email");
        credentials.setPassword("password");

        String jsonRequest = objectMapper.writeValueAsString(credentials);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("El email debe ser válido"))
                .andExpect(jsonPath("$.jwt-token").doesNotExist())
                .andExpect(jsonPath("$.user").doesNotExist());
    }

    // Tests 2.3.2
    @Test
    void testMailYContrasenaValidos() throws Exception {
        RegisterUserDTO registerUser = new RegisterUserDTO();
        registerUser.setFirstName("Juan");
        registerUser.setLastName("Pérez");
        registerUser.setEmail("juan.perez@email.com");
        registerUser.setPhoneNumber("123456789");
        registerUser.setPassword("Contraseña123");
        registerUser.setPasswordConfirmation("Contraseña123");

        String jsonRequest = objectMapper.writeValueAsString(registerUser);

        long countBefore = userRepository.count();

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Usuario registrado con éxito"));

        long countAfter = userRepository.count();
        assert(countAfter == countBefore + 1);

        boolean userExists = userRepository.findByEmailAndStatus("juan.perez@email.com", EntityStatus.ACTIVE)
                .isPresent();
        assert(userExists);
    }

    @Test
    void testMailValidoYContrasenaInvalida() throws Exception {
        RegisterUserDTO registerUser = new RegisterUserDTO();
        registerUser.setFirstName("Juan");
        registerUser.setLastName("Pérez");
        registerUser.setEmail("juan.perez2@email.com");
        registerUser.setPhoneNumber("987654321");
        registerUser.setPassword("123");
        registerUser.setPasswordConfirmation("123");

        String jsonRequest = objectMapper.writeValueAsString(registerUser);

        long countBefore = userRepository.count();

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.password").value("La contraseña debe tener entre 8 y 20 caracteres"));

        long countAfter = userRepository.count();
        assert(countAfter == countBefore);

        boolean userExists = userRepository.findByEmailAndStatus("juan.perez2@email.com", EntityStatus.ACTIVE)
                .isPresent();
        assert(!userExists);
    }

    @Test
    void testMailInvalidoYContrasenaValida() throws Exception {
        RegisterUserDTO registerUser = new RegisterUserDTO();
        registerUser.setFirstName("Juan");
        registerUser.setLastName("Pérez");
        registerUser.setEmail("juan.perez@email");
        registerUser.setPhoneNumber("123456789");
        registerUser.setPassword("Contraseña123");
        registerUser.setPasswordConfirmation("Contraseña123");

        String jsonRequest = objectMapper.writeValueAsString(registerUser);

        long countBefore = userRepository.count();

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("El email debe ser válido"));

        long countAfter = userRepository.count();
        assert(countAfter == countBefore);

        boolean userExists = userRepository.findByEmailAndStatus("juan.perez@email", EntityStatus.ACTIVE)
                .isPresent();
        assert(!userExists);
    }

    @Test
    void testMailYContrasenaInvalido() throws Exception {
        RegisterUserDTO registerUser = new RegisterUserDTO();
        registerUser.setFirstName("Juan");
        registerUser.setLastName("Pérez");
        registerUser.setEmail("juan.perez@email");
        registerUser.setPhoneNumber("123456789");
        registerUser.setPassword("123");
        registerUser.setPasswordConfirmation("123");

        String jsonRequest = objectMapper.writeValueAsString(registerUser);

        long countBefore = userRepository.count();

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("El email debe ser válido"))
                .andExpect(jsonPath("$.password").value("La contraseña debe tener entre 8 y 20 caracteres"));

        long countAfter = userRepository.count();
        assert(countAfter == countBefore);

        boolean userExists = userRepository.findByEmailAndStatus("juan.perez@email", EntityStatus.ACTIVE)
                .isPresent();
        assert(!userExists);
    }
}
