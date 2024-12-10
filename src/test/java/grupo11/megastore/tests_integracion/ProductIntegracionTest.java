package grupo11.megastore.tests_integracion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import grupo11.megastore.products.dto.product.CreateProductDTO;
import grupo11.megastore.products.model.Brand;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.model.repository.BrandRepository;
import grupo11.megastore.products.model.repository.CategoryRepository;
import grupo11.megastore.products.model.repository.ProductRepository;
import grupo11.megastore.products.model.repository.SubcategoryRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
public class ProductIntegracionTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    
    @Autowired
    private BrandRepository brandRepository;
    
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        subcategoryRepository.deleteAll();
        brandRepository.deleteAll();
        
        Category category = new Category();
        category.setName("Calzado");
        categoryRepository.save(category);
        
        Subcategory subcategory = new Subcategory();
        subcategory.setName("Deportivo");
        subcategory.setCategory(category);
        subcategoryRepository.save(subcategory);
        
        Brand brand = new Brand();
        brand.setName("Nike");
        brandRepository.save(brand);
    }

    // Tests 2.1.2
    @Test
    void testPrecioValorMinimo() throws Exception {
        CreateProductDTO createProductDTO = new CreateProductDTO();
        createProductDTO.setName("Zapatillas");
        createProductDTO.setDescription("Zapatillas deportivas de alta calidad");
        createProductDTO.setPrice(0.1);
        createProductDTO.setCategoryId(categoryRepository.findAll().get(0).getId());
        createProductDTO.setSubcategoryId(subcategoryRepository.findAll().get(0).getId());
        createProductDTO.setBrandId(brandRepository.findAll().get(0).getId());

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createProductDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(0.1));
    }

    @Test
    void testPrecioCero() throws Exception {
        CreateProductDTO createProductDTO = new CreateProductDTO();
        createProductDTO.setName("Zapatillas");
        createProductDTO.setDescription("Zapatillas deportivas de alta calidad");
        createProductDTO.setPrice(0.0);
        createProductDTO.setCategoryId(categoryRepository.findAll().get(0).getId());
        createProductDTO.setSubcategoryId(subcategoryRepository.findAll().get(0).getId());
        createProductDTO.setBrandId(brandRepository.findAll().get(0).getId());

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createProductDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.price").value("El precio debe ser positivo"));
    }
}
