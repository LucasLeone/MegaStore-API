package grupo11.megastore.tests_integracion;

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
import grupo11.megastore.sales.model.Sale;
import grupo11.megastore.sales.model.SaleDetail;
import grupo11.megastore.sales.model.repository.SaleRepository;
import grupo11.megastore.users.model.User;
import grupo11.megastore.users.model.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
public class SalesIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        saleRepository.deleteAll();
        productRepository.deleteAll();
        variantRepository.deleteAll();
        categoryRepository.deleteAll();
        subcategoryRepository.deleteAll();
        brandRepository.deleteAll();
        userRepository.deleteAll();

        // Crear Usuario
        User user = new User();
        user.setEmail("admin@example.com");
        user.setPassword("password");
        user.setPhoneNumber("21312312321");
        user.setStatus(EntityStatus.ACTIVE);
        userRepository.save(user);

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
        product.setPrice(100.00);
        productRepository.save(product);

        // Crear Variante con Stock de 10 unidades
        Variant variant = new Variant();
        variant.setProduct(product);
        variant.setStock(10);
        variant.setSize("42");
        variant.setColor("Negro");
        variant.setStatus(EntityStatus.ACTIVE);
        variantRepository.save(variant);

        // Crear Ventas en fechas específicas
        createSale(
                user,
                LocalDateTime.of(2024, 12, 31, 15, 30), // Fecha límite
                variant,
                2,
                new BigDecimal("10.00"),
                "Envío Estándar",
                "Juan Pérez",
                "Calle Falsa 123",
                "Buenos Aires",
                "Buenos Aires",
                "C1000AAA",
                "Argentina");

        createSale(
                user,
                LocalDateTime.of(2024, 12, 30, 10, 0), // Fecha anterior a la límite
                variant,
                1,
                new BigDecimal("5.00"),
                "Envío Exprés",
                "María López",
                "Avenida Siempre Viva 742",
                "Córdoba",
                "Córdoba",
                "X5000",
                "Argentina");
    }

    private Sale createSale(
            User user,
            LocalDateTime saleDate,
            Variant variant,
            int quantity,
            BigDecimal shippingCost,
            String shippingMethod,
            String fullName,
            String address,
            String city,
            String state,
            String postalCode,
            String country) {

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setSaleDate(saleDate);
        sale.setPaymentMethod("Tarjeta de Crédito");
        sale.setShippingCost(shippingCost);
        sale.setShippingMethod(shippingMethod);
        sale.setFullName(fullName);
        sale.setAddress(address);
        sale.setCity(city);
        sale.setState(state);
        sale.setPostalCode(postalCode);
        sale.setCountry(country);

        SaleDetail saleDetail = new SaleDetail();
        saleDetail.setVariant(variant);
        saleDetail.setQuantity(quantity);
        saleDetail.setUnitPrice(new BigDecimal(variant.getProduct().getPrice()));

        sale.addSaleDetail(saleDetail);
        sale.calculateTotalAmount();

        return saleRepository.save(sale);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testReporteVentas_FechaIgualALimite() throws Exception {
        String fechaFiltro = "2024-12-31";

        mockMvc.perform(get("/sales/reports")
                .param("startDate", fechaFiltro)
                .param("endDate", fechaFiltro)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOrders").value(1))
                .andExpect(jsonPath("$.totalSales").value(210.00))
                .andExpect(jsonPath("$.topProducts.Zapatillas").value(2))
                .andExpect(jsonPath("$.averageOrderValue").value(210.00));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testReporteVentas_FechaAnteriorALimite() throws Exception {
        String fechaFiltro = "2024-12-30";

        mockMvc.perform(get("/sales/reports")
                .param("startDate", fechaFiltro)
                .param("endDate", fechaFiltro)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOrders").value(1))
                .andExpect(jsonPath("$.totalSales").value(105.00))
                .andExpect(jsonPath("$.topProducts.Zapatillas").value(1))
                .andExpect(jsonPath("$.averageOrderValue").value(105.00));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testReporteVentas_FechaPosteriorALimite_NoVentas() throws Exception {
        String fechaFiltro = "2025-01-01";

        mockMvc.perform(get("/sales/reports")
                .param("startDate", fechaFiltro)
                .param("endDate", fechaFiltro)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOrders").value(0))
                .andExpect(jsonPath("$.totalSales").value(0.00))
                .andExpect(jsonPath("$.topProducts").isEmpty())
                .andExpect(jsonPath("$.averageOrderValue").value(0.0));
    }
}
