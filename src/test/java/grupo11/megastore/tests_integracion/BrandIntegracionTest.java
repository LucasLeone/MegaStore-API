package grupo11.megastore.tests_integracion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import grupo11.megastore.config.TestConfig;
import grupo11.megastore.products.dto.brand.CreateBrandDTO;
import grupo11.megastore.products.model.repository.BrandRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
public class BrandIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void setup() {
        brandRepository.deleteAll();
    }

    // Tests 2.1.1
    @Test
    void testNombreValido() throws Exception {
        CreateBrandDTO createBrandDTO = new CreateBrandDTO();
        createBrandDTO.setName("Nike");

        mockMvc.perform(post("/brands").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createBrandDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nike"));
    }

    @Test
    void testNombreInvalido() throws Exception {
        CreateBrandDTO createBrandDTO = new CreateBrandDTO();
        createBrandDTO.setName("ni");

        mockMvc.perform(post("/brands").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createBrandDTO)))
                .andExpect(status().isBadRequest());
    }
}
