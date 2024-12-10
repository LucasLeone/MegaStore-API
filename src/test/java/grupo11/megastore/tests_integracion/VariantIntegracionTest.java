package grupo11.megastore.tests_integracion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.products.dto.variant.CreateVariantDTO;
import grupo11.megastore.products.model.Brand;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.model.repository.BrandRepository;
import grupo11.megastore.products.model.repository.CategoryRepository;
import grupo11.megastore.products.model.repository.ProductRepository;
import grupo11.megastore.products.model.repository.SubcategoryRepository;
import grupo11.megastore.products.model.repository.VariantRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
public class VariantIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product existingProduct;

    @BeforeEach
    void setup() {
        variantRepository.deleteAll();
        productRepository.deleteAll();
        brandRepository.deleteAll();
        categoryRepository.deleteAll();

        // Crear Categoría
        Category category = new Category();
        category.setName("TestCategory");
        category.setDescription("Descripción de TestCategory");
        category.setStatus(EntityStatus.ACTIVE);
        category = categoryRepository.save(category);

        // Crear Subcategoria
        Subcategory subcategory = new Subcategory();
        subcategory.setName("TestSubcategory");
        subcategory.setDescription("Descripción de TestSubcategory");
        subcategory.setCategory(category);
        subcategory.setStatus(EntityStatus.ACTIVE);
        subcategory = subcategoryRepository.save(subcategory);

        // Crear Marca
        Brand brand = new Brand();
        brand.setName("TestBrand");
        brand.setDescription("Descripción de TestBrand");
        brand.setStatus(EntityStatus.ACTIVE);
        brand = brandRepository.save(brand);

        // Crear Producto
        Product product = new Product();
        product.setName("TestProduct");
        product.setDescription("Descripción de TestProduct");
        product.setBrand(brand);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setPrice(100.0);
        product.setStatus(EntityStatus.ACTIVE);
        existingProduct = productRepository.save(product);
    }

    // Tests 2.2.4
    @Test
    void testColorSinEspacios() throws Exception {
        CreateVariantDTO createVariantDTO = new CreateVariantDTO();
        createVariantDTO.setProductId(existingProduct.getId());
        createVariantDTO.setColor("Rojo");
        createVariantDTO.setSize("M");
        createVariantDTO.setStock(10);

        mockMvc.perform(post("/variants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createVariantDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.color").value("Rojo"))
                .andExpect(jsonPath("$.size").value("M"))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void testColorConEspaciosIniciales() throws Exception {
        CreateVariantDTO createVariantDTO = new CreateVariantDTO();
        createVariantDTO.setProductId(existingProduct.getId());
        createVariantDTO.setColor("  Azul");
        createVariantDTO.setSize("M");
        createVariantDTO.setStock(10);

        mockMvc.perform(post("/variants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createVariantDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El color y el tamaño no pueden empezar o terminar con espacios"));
    }

    @Test
    void testColorConEspaciosFinales() throws Exception {
        CreateVariantDTO createVariantDTO = new CreateVariantDTO();
        createVariantDTO.setProductId(existingProduct.getId());
        createVariantDTO.setColor("Verde ");
        createVariantDTO.setSize("M");
        createVariantDTO.setStock(10);

        mockMvc.perform(post("/variants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createVariantDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El color y el tamaño no pueden empezar o terminar con espacios"));
    }
}
