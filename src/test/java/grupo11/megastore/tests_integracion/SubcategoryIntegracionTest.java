package grupo11.megastore.tests_integracion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import grupo11.megastore.config.TestConfig;
import grupo11.megastore.products.dto.subcategory.UpdateSubcategoryDTO;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.model.repository.CategoryRepository;
import grupo11.megastore.products.model.repository.SubcategoryRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
public class SubcategoryIntegracionTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long subcategoryId;

    @BeforeEach
    void setup() {
        subcategoryRepository.deleteAll();
        categoryRepository.deleteAll();

        // Crear categoría
        Category category = new Category();
        category.setName("Pantalones");
        Category savedCategory = categoryRepository.save(category);

        // Crear subcategoría
        Subcategory subcategory = new Subcategory();
        subcategory.setName("Jeans");
        subcategory.setCategory(savedCategory);
        Subcategory savedSubcategory = subcategoryRepository.save(subcategory);
        this.subcategoryId = savedSubcategory.getId();
    }

    // Tests 2.1.3
    @Test
    void testCrearNombreLongMaxima() throws Exception {
        String nombreMaximo = "NombreDeSubcategoriaDe32Caracter"; // 32 caracteres

        UpdateSubcategoryDTO updateSubcategoryDTO = new UpdateSubcategoryDTO();
        updateSubcategoryDTO.setName(nombreMaximo);

        mockMvc.perform(put("/subcategories/" + subcategoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSubcategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(nombreMaximo));
    }

    @Test
    void testCrearNombreLongMaximaMasUno() throws Exception {
        String nombreExcedeMaximo = "NombreDeSubcategoriaDe33Caractere"; // 33 caracteres

        UpdateSubcategoryDTO updateSubcategoryDTO = new UpdateSubcategoryDTO();
        updateSubcategoryDTO.setName(nombreExcedeMaximo);

        mockMvc.perform(put("/subcategories/" + subcategoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSubcategoryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("El nombre debe tener entre 2 y 32 caracteres"));
    }

    @Test
    void testCrearNombreLongMaximaMenosUno() throws Exception {
        String nombreMenosUno = "NombreDeSubcategoriaDe31Caracte"; // 31 caracteres

        UpdateSubcategoryDTO updateSubcategoryDTO = new UpdateSubcategoryDTO();
        updateSubcategoryDTO.setName(nombreMenosUno);

        mockMvc.perform(put("/subcategories/" + subcategoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSubcategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(nombreMenosUno));
    }
}
