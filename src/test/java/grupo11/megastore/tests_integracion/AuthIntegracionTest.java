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

import grupo11.megastore.users.dto.user.LoginCredentials;

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
}
