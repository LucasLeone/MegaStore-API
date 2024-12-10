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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import grupo11.megastore.carts.model.Cart;
import grupo11.megastore.carts.model.repository.CartRepository;
import grupo11.megastore.carts.model.repository.CartItemRepository;
import grupo11.megastore.config.TestConfig;
import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.products.model.Brand;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.model.Variant;
import grupo11.megastore.products.model.repository.BrandRepository;
import grupo11.megastore.products.model.repository.CategoryRepository;
import grupo11.megastore.products.model.repository.ProductRepository;
import grupo11.megastore.products.model.repository.SubcategoryRepository;
import grupo11.megastore.products.model.repository.VariantRepository;
import grupo11.megastore.users.model.User;
import grupo11.megastore.users.model.repository.UserRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
public class CartIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private UserRepository userRepository;

    private Long variantId;

    @BeforeEach
    void setup() {
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        variantRepository.deleteAll();
        categoryRepository.deleteAll();
        subcategoryRepository.deleteAll();
        brandRepository.deleteAll();
        userRepository.deleteAll();

        // Crear Categoría
        Category category = new Category();
        category.setName("Calzado");
        categoryRepository.save(category);

        // Crear Subcategoría
        Subcategory subcategory = new Subcategory();
        subcategory.setName("Deportivo");
        subcategory.setCategory(category);
        subcategoryRepository.save(subcategory);

        // Crear Marca
        Brand brand = new Brand();
        brand.setName("Nike");
        brandRepository.save(brand);

        // Crear Producto
        Product product = new Product();
        product.setName("Zapatillas");
        product.setBrand(brand);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setPrice(100.0);
        productRepository.save(product);

        // Crear Variante con Stock de 10 unidades
        Variant variant = new Variant();
        variant.setProduct(product);
        variant.setStock(10);
        variant.setSize("42");
        variant.setColor("Negro");
        variant.setStatus(EntityStatus.ACTIVE);
        variantRepository.save(variant);
        this.variantId = variant.getId();

        // Crear Usuario
        User user = new User();
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setPhoneNumber("21312312321");
        user.setStatus(EntityStatus.ACTIVE);
        userRepository.save(user);

        // Asignar Carrito al Usuario
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
        user.setCart(cart);
        userRepository.save(user);
    }

    // Tests 2.1.4
    @Test
    @WithMockUser(username = "testuser@example.com", roles = { "USER" })
    void testCantidadIgualAlStock() throws Exception {
        Long variantIdToAdd = this.variantId;
        Integer quantity = 10;

        mockMvc.perform(post("/carts/items/" + variantIdToAdd + "/quantity/" + quantity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems").isArray())
                .andExpect(jsonPath("$.cartItems[0].variant.id").value(variantIdToAdd))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(quantity))
                .andExpect(jsonPath("$.total").value(100.0 * quantity));
    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = { "USER" })
    void testCantidadMayorAlStock() throws Exception {
        Long variantIdToAdd = this.variantId;
        Integer quantity = 11;

        mockMvc.perform(post("/carts/items/" + variantIdToAdd + "/quantity/" + quantity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Por favor, realiza un pedido de la variante " + variantIdToAdd
                                + " menor o igual a la cantidad 10."));
    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = { "USER" })
    void testCantidadMenorAlStock() throws Exception {
        Long variantIdToAdd = this.variantId;
        Integer quantity = 5;

        mockMvc.perform(post("/carts/items/" + variantIdToAdd + "/quantity/" + quantity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems").isArray())
                .andExpect(jsonPath("$.cartItems[0].variant.id").value(variantIdToAdd))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(quantity))
                .andExpect(jsonPath("$.total").value(100.0 * quantity));
    }
}
