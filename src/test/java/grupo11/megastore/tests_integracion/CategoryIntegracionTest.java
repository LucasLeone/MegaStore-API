package grupo11.megastore.tests_integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import grupo11.megastore.config.TestConfig;
import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.model.repository.CategoryRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
public class CategoryIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        categoryRepository.deleteAll();
    }

    // Tests 2.2.2
    @Test
    void testNombreRangoPermitido() throws Exception {
        Category category = new Category();
        category.setName("Categoria valida");
        category.setDescription("Test descripcion");

        String jsonRequest = objectMapper.writeValueAsString(category);

        mockMvc.perform(post("/categories")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Categoria valida"))
                .andExpect(jsonPath("$.description").value("Test descripcion"));
    }

    @Test
    void testNombreRangoMenorPermitido() throws Exception {
        Category category = new Category();
        category.setName("A");
        category.setDescription("Test descripcion");

        String jsonRequest = objectMapper.writeValueAsString(category);

        mockMvc.perform(post("/categories")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNombreRangoMayorPermitido() throws Exception {
        Category category = new Category();
        category.setName("Categoría muy larga que excede el límite permitido");
        category.setDescription("Test descripcion");

        String jsonRequest = objectMapper.writeValueAsString(category);

        mockMvc.perform(post("/categories")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    // Tests 2.4.5

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testRestaurarCategoriaEliminadaConExito() throws Exception {
        Category deletedCategory = new Category();
        deletedCategory.setName("Deportes");
        deletedCategory.setDescription("Categoría de productos deportivos");
        deletedCategory.setStatus(EntityStatus.DELETED);
        deletedCategory = categoryRepository.save(deletedCategory);
        Long categoryId = deletedCategory.getId();

        mockMvc.perform(post("/categories/" + categoryId + "/restore")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Category updatedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", categoryId));
        assertEquals(EntityStatus.ACTIVE, updatedCategory.getStatus(), "El estado de la categoría debería ser ACTIVE");
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testIntentarRestaurarCategoriaYaActiva() throws Exception {
        Category activeCategory = new Category();
        activeCategory.setName("Electrónica");
        activeCategory.setDescription("Categoría de productos electrónicos");
        activeCategory.setStatus(EntityStatus.ACTIVE);
        activeCategory = categoryRepository.save(activeCategory);
        Long categoryId = activeCategory.getId();

        mockMvc.perform(post("/categories/" + categoryId + "/restore")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Category updatedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", categoryId));
        assertEquals(EntityStatus.ACTIVE, updatedCategory.getStatus(),
                "El estado de la categoría debería seguir siendo ACTIVE");
    }
}
