package grupo11.megastore.tests_integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import grupo11.megastore.products.dto.product.CreateProductDTO;
import grupo11.megastore.products.dto.product.UpdateProductDTO;
import grupo11.megastore.products.model.Brand;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.model.repository.BrandRepository;
import grupo11.megastore.products.model.repository.CategoryRepository;
import grupo11.megastore.products.model.repository.ProductRepository;
import grupo11.megastore.products.model.repository.SubcategoryRepository;
import grupo11.megastore.constant.EntityStatus;

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

    private Long validCategoryId;
    private Long validSubcategoryId;
    private Long validBrandId;
    private Long productId;
    private final Long invalidCategoryId = 999L;
    private final Long invalidSubcategoryId = 999L;
    private final Long invalidBrandId = 999L;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        subcategoryRepository.deleteAll();
        brandRepository.deleteAll();

        Category category = new Category();
        category.setName("Calzado");
        category = categoryRepository.save(category);
        validCategoryId = category.getId();

        Subcategory subcategory = new Subcategory();
        subcategory.setName("Deportivo");
        subcategory.setCategory(category);
        subcategory = subcategoryRepository.save(subcategory);
        validSubcategoryId = subcategory.getId();

        Brand brand = new Brand();
        brand.setName("Nike");
        brand = brandRepository.save(brand);
        validBrandId = brand.getId();

        Product product = new Product();
        product.setName("Zapatillas Originales");
        product.setDescription("Zapatillas deportivas de alta calidad");
        product.setPrice(100.0);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setBrand(brand);
        product = productRepository.save(product);
        productId = product.getId();
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

    // Tests 2.2.5
    @Test
    void testPrecioPositivo() throws Exception {
        long countBefore = productRepository.count();

        CreateProductDTO createProductDTO = new CreateProductDTO();
        createProductDTO.setName("Producto A");
        createProductDTO.setDescription("Descripción del producto A");
        createProductDTO.setPrice(100.0);
        createProductDTO.setCategoryId(categoryRepository.findAll().get(0).getId());
        createProductDTO.setSubcategoryId(subcategoryRepository.findAll().get(0).getId());
        createProductDTO.setBrandId(brandRepository.findAll().get(0).getId());

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createProductDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(100.0));

        long countAfter = productRepository.count();
        assertEquals(countBefore + 1, countAfter, "El conteo de productos debería haber incrementado en 1");
    }

    @Test
    void testPrecioNegativo() throws Exception {
        long countBefore = productRepository.count();

        CreateProductDTO createProductDTO = new CreateProductDTO();
        createProductDTO.setName("Producto A");
        createProductDTO.setDescription("Descripción del producto A");
        createProductDTO.setPrice(-100.0);
        createProductDTO.setCategoryId(categoryRepository.findAll().get(0).getId());
        createProductDTO.setSubcategoryId(subcategoryRepository.findAll().get(0).getId());
        createProductDTO.setBrandId(brandRepository.findAll().get(0).getId());

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createProductDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.price").value("El precio debe ser positivo"));

        long countAfter = productRepository.count();
        assertEquals(countAfter, countBefore, "No se debe haber creado un producto con precio negativo");
    }

    // Tests 2.3.3
    @Test
    void testActualizarConMarcaCategoriaSubcategoriaValidos() throws Exception {
        UpdateProductDTO updateProduct = new UpdateProductDTO();
        updateProduct.setName("Producto Actualizado");
        updateProduct.setDescription("Descripción actualizada del producto");
        updateProduct.setPrice(150.0);
        updateProduct.setCategoryId(validCategoryId);
        updateProduct.setSubcategoryId(validSubcategoryId);
        updateProduct.setBrandId(validBrandId);

        String jsonRequest = objectMapper.writeValueAsString(updateProduct);

        mockMvc.perform(put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Producto Actualizado"))
                .andExpect(jsonPath("$.description").value("Descripción actualizada del producto"))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.category.id").value(validCategoryId))
                .andExpect(jsonPath("$.subcategory.id").value(validSubcategoryId))
                .andExpect(jsonPath("$.brand.id").value(validBrandId));

        Product updatedProduct = productRepository.findById(productId).orElseThrow();
        assertEquals("Producto Actualizado", updatedProduct.getName());
        assertEquals("Descripción actualizada del producto", updatedProduct.getDescription());
        assertEquals(150.0, updatedProduct.getPrice());
        assertEquals(validCategoryId, updatedProduct.getCategory().getId());
        assertEquals(validSubcategoryId, updatedProduct.getSubcategory().getId());
        assertEquals(validBrandId, updatedProduct.getBrand().getId());
    }

    @Test
    void testActualizarConCategoriaSubcategoriaValidasYMarcaInvalida() throws Exception {
        UpdateProductDTO updateProduct = new UpdateProductDTO();
        updateProduct.setName("Producto Actualizado");
        updateProduct.setDescription("Descripción actualizada del producto");
        updateProduct.setPrice(150.0);
        updateProduct.setCategoryId(validCategoryId);
        updateProduct.setSubcategoryId(validSubcategoryId);
        updateProduct.setBrandId(invalidBrandId);

        String jsonRequest = objectMapper.writeValueAsString(updateProduct);

        mockMvc.perform(put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Marca no encontrado con id: " + invalidBrandId));
    }

    @Test
    void testActualizarConCategoriaMarcaValidasYSubcategoriaInvalida() throws Exception {
        UpdateProductDTO updateProduct = new UpdateProductDTO();
        updateProduct.setName("Producto Actualizado");
        updateProduct.setDescription("Descripción actualizada del producto");
        updateProduct.setPrice(150.0);
        updateProduct.setCategoryId(validCategoryId);
        updateProduct.setSubcategoryId(invalidSubcategoryId);
        updateProduct.setBrandId(validBrandId);

        String jsonRequest = objectMapper.writeValueAsString(updateProduct);

        mockMvc.perform(put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Subcategoría no encontrado con id: " + invalidSubcategoryId));
    }

    @Test
    void testActualizarConCategoriaInvalidaYSubcategoriaMarcaValida() throws Exception {
        UpdateProductDTO updateProduct = new UpdateProductDTO();
        updateProduct.setName("Producto Actualizado");
        updateProduct.setDescription("Descripción actualizada del producto");
        updateProduct.setPrice(150.0);
        updateProduct.setCategoryId(invalidCategoryId);
        updateProduct.setSubcategoryId(validSubcategoryId);
        updateProduct.setBrandId(validBrandId);

        String jsonRequest = objectMapper.writeValueAsString(updateProduct);

        mockMvc.perform(put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoría no encontrado con id: " + invalidCategoryId));
    }

    @Test
    void testActualizarConCategoriaSubcategoriaInvalidasYMarcaValida() throws Exception {
        UpdateProductDTO updateProduct = new UpdateProductDTO();
        updateProduct.setName("Producto Actualizado");
        updateProduct.setDescription("Descripción actualizada del producto");
        updateProduct.setPrice(150.0);
        updateProduct.setCategoryId(invalidCategoryId);
        updateProduct.setSubcategoryId(invalidSubcategoryId);
        updateProduct.setBrandId(validBrandId);

        String jsonRequest = objectMapper.writeValueAsString(updateProduct);

        mockMvc.perform(put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoría no encontrado con id: " + invalidCategoryId));
    }

    @Test
    void testActualizarConCategoriaValidaYSubcategoriaMarcaInvalidas() throws Exception {
        UpdateProductDTO updateProduct = new UpdateProductDTO();
        updateProduct.setName("Producto Actualizado");
        updateProduct.setDescription("Descripción actualizada del producto");
        updateProduct.setPrice(150.0);
        updateProduct.setCategoryId(validCategoryId);
        updateProduct.setSubcategoryId(invalidSubcategoryId);
        updateProduct.setBrandId(invalidBrandId);

        String jsonRequest = objectMapper.writeValueAsString(updateProduct);

        mockMvc.perform(put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Subcategoría no encontrado con id: " + invalidSubcategoryId));
    }

    @Test
    void testActualizarConMarcaCategoriaInvalidaYSubcategoriaValida() throws Exception {
        UpdateProductDTO updateProduct = new UpdateProductDTO();
        updateProduct.setName("Producto Actualizado");
        updateProduct.setDescription("Descripción actualizada del producto");
        updateProduct.setPrice(150.0);
        updateProduct.setCategoryId(invalidCategoryId);
        updateProduct.setSubcategoryId(validSubcategoryId);
        updateProduct.setBrandId(invalidBrandId);

        String jsonRequest = objectMapper.writeValueAsString(updateProduct);

        mockMvc.perform(put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoría no encontrado con id: " + invalidCategoryId));
    }

    @Test
    void testActualizarConMarcaCategoriaSubcategoriaInvalidos() throws Exception {
        UpdateProductDTO updateProduct = new UpdateProductDTO();
        updateProduct.setName("Producto Actualizado");
        updateProduct.setDescription("Descripción actualizada del producto");
        updateProduct.setPrice(150.0);
        updateProduct.setCategoryId(invalidCategoryId);
        updateProduct.setSubcategoryId(invalidSubcategoryId);
        updateProduct.setBrandId(invalidBrandId);

        String jsonRequest = objectMapper.writeValueAsString(updateProduct);

        mockMvc.perform(put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoría no encontrado con id: " + invalidCategoryId));
    }

    // Tests 2.5.2
    @Test
    public void testFiltrarProductosPorCategoriaInexistente() throws Exception {
        Long inexistenteCategoryId = 9999L;

        boolean exists = categoryRepository.findById(inexistenteCategoryId).isPresent();
        assertEquals(false, exists, "No debe existir una categoría con ID: " + inexistenteCategoryId);

        mockMvc.perform(get("/products")
                .param("categoryId", String.valueOf(inexistenteCategoryId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testFiltrarProductosPorCategoriaSinProductos() throws Exception {
        Category categoriaSinProductos = new Category();
        categoriaSinProductos.setName("Categoría Sin Productos");
        categoriaSinProductos = categoryRepository.save(categoriaSinProductos);
        Long categoriaSinProductosId = categoriaSinProductos.getId();

        long productosCount = productRepository.findByBrandIdAndStatus(categoriaSinProductosId, EntityStatus.ACTIVE)
                .size();
        assertEquals(0, productosCount, "La categoría debe no tener productos asociados");

        mockMvc.perform(get("/products")
                .param("categoryId", String.valueOf(categoriaSinProductosId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testFiltrarProductosPorCategoriaConProductos() throws Exception {
        Category categoriaConProductos = new Category();
        categoriaConProductos.setName("Categoría Con Productos");
        categoriaConProductos = categoryRepository.save(categoriaConProductos);
        Long categoriaConProductosId = categoriaConProductos.getId();

        Subcategory subcategory = new Subcategory();
        subcategory.setName("Subcategoría Deportiva");
        subcategory.setCategory(categoriaConProductos);
        subcategory = subcategoryRepository.save(subcategory);

        Brand brand = new Brand();
        brand.setName("Adidas");
        brand = brandRepository.save(brand);

        Product producto1 = new Product();
        producto1.setName("Producto 1");
        producto1.setDescription("Descripción del Producto 1");
        producto1.setPrice(50.0);
        producto1.setCategory(categoriaConProductos);
        producto1.setSubcategory(subcategory);
        producto1.setBrand(brand);
        productRepository.save(producto1);

        Product producto2 = new Product();
        producto2.setName("Producto 2");
        producto2.setDescription("Descripción del Producto 2");
        producto2.setPrice(75.0);
        producto2.setCategory(categoriaConProductos);
        producto2.setSubcategory(subcategory);
        producto2.setBrand(brand);
        productRepository.save(producto2);

        mockMvc.perform(get("/products")
                .param("categoryId", String.valueOf(categoriaConProductosId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Producto 1"))
                .andExpect(jsonPath("$[1].name").value("Producto 2"));
    }
}
