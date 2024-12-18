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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import grupo11.megastore.sales.constant.SaleStatus;
import grupo11.megastore.sales.dto.sale.CreateSaleDTO;
import grupo11.megastore.sales.dto.sale.ShippingInfoDTO;
import grupo11.megastore.sales.dto.saleDetail.CreateSaleDetailDTO;
import grupo11.megastore.sales.model.Sale;
import grupo11.megastore.sales.model.SaleDetail;
import grupo11.megastore.sales.model.repository.SaleRepository;
import grupo11.megastore.users.model.User;
import grupo11.megastore.users.model.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        saleRepository.deleteAll();
        productRepository.deleteAll();
        variantRepository.deleteAll();
        categoryRepository.deleteAll();
        subcategoryRepository.deleteAll();
        brandRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setEmail("admin@example.com");
        user.setPassword("password");
        user.setPhoneNumber("21312312321");
        user.setStatus(EntityStatus.ACTIVE);
        userRepository.save(user);

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

        Product product = new Product();
        product.setName("Zapatillas");
        product.setBrand(brand);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setPrice(100.00);
        productRepository.save(product);

        Variant variant = new Variant();
        variant.setProduct(product);
        variant.setStock(10);
        variant.setSize("42");
        variant.setColor("Negro");
        variant.setStatus(EntityStatus.ACTIVE);
        variantRepository.save(variant);

        createSale(
                user,
                LocalDateTime.of(2024, 12, 31, 15, 30),
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
                LocalDateTime.of(2024, 12, 30, 10, 0),
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

    // Tests 2.1.5
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

    private CreateSaleDTO crearVentaDTO(String paymentMethod, String shippingMethod) {
        CreateSaleDTO createSaleDTO = new CreateSaleDTO();
        createSaleDTO.setUserId(obtenerIdUsuario());
        createSaleDTO.setPaymentMethod(paymentMethod);
        createSaleDTO.setShippingCost(new BigDecimal("10.00"));
        createSaleDTO.setShippingMethod(shippingMethod);

        CreateSaleDetailDTO detalle = new CreateSaleDetailDTO();
        detalle.setVariantId(obtenerIdVariante());
        detalle.setQuantity(1);

        ShippingInfoDTO shippingInfo = new ShippingInfoDTO();
        shippingInfo.setFullName("Juan Pérez");
        shippingInfo.setAddress("Calle Falsa 123");
        shippingInfo.setCity("Buenos Aires");
        shippingInfo.setState("Buenos Aires");
        shippingInfo.setPostalCode("C1000AAA");
        shippingInfo.setCountry("Argentina");

        createSaleDTO.setSaleDetails(Arrays.asList(detalle));
        createSaleDTO.setShippingInfo(shippingInfo);

        return createSaleDTO;
    }

    private Long obtenerIdUsuario() {
        return userRepository.findAll().get(0).getId();
    }

    private Long obtenerIdVariante() {
        return variantRepository.findAll().get(0).getId();
    }

    // Tests 2.3.1
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testMetodoEnvioYPagoValidos() throws Exception {
        long countBefore = saleRepository.count();

        CreateSaleDTO createSaleDTO = crearVentaDTO("Tarjeta de Crédito", "Envío Exprés");

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.paymentMethod").value("Tarjeta de Crédito"))
                .andExpect(jsonPath("$.shippingMethod").value("Envío Exprés"))
                .andExpect(jsonPath("$.fullName").value("Juan Pérez"))
                .andExpect(jsonPath("$.address").value("Calle Falsa 123"))
                .andExpect(jsonPath("$.city").value("Buenos Aires"))
                .andExpect(jsonPath("$.state").value("Buenos Aires"))
                .andExpect(jsonPath("$.postalCode").value("C1000AAA"))
                .andExpect(jsonPath("$.country").value("Argentina"))
                .andExpect(jsonPath("$.saleDetails").isArray())
                .andExpect(jsonPath("$.saleDetails[0].variant.id").value(obtenerIdVariante()))
                .andExpect(jsonPath("$.saleDetails[0].quantity").value(1))
                .andExpect(jsonPath("$.totalAmount").value(110.00));

        long countAfter = saleRepository.count();
        assertEquals(countBefore + 1, countAfter, "La venta debería haber sido registrada en el sistema");
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testMetodoPagoValidoMetodoEnvioInvalido() throws Exception {
        long countBefore = saleRepository.count();

        CreateSaleDTO createSaleDTO = crearVentaDTO("Tarjeta de Crédito", null);

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.shippingMethod").value("El método de envío no puede ser nulo"));

        long countAfter = saleRepository.count();
        assertEquals(countBefore, countAfter, "La venta no debería haber sido registrada en el sistema");
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testMetodoPagoInvalidoMetodoEnvioValido() throws Exception {
        long countBefore = saleRepository.count();

        CreateSaleDTO createSaleDTO = crearVentaDTO(null, "Envío Exprés");

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.paymentMethod").value("El método de pago no puede ser nulo"));

        long countAfter = saleRepository.count();
        assertEquals(countBefore, countAfter, "La venta no debería haber sido registrada en el sistema");
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testAmbosMetodosInvalido() throws Exception {
        long countBefore = saleRepository.count();

        CreateSaleDTO createSaleDTO = crearVentaDTO(null, null);

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.paymentMethod").value("El método de pago no puede ser nulo"))
                .andExpect(jsonPath("$.shippingMethod").value("El método de envío no puede ser nulo"));

        long countAfter = saleRepository.count();
        assertEquals(countBefore, countAfter, "La venta no debería haber sido registrada en el sistema");
    }

    // Tests 2.4.2
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testCancelarVentaEnEstadoInProcess() throws Exception {
        Sale sale = createSale(
                userRepository.findAll().get(0),
                LocalDateTime.now(),
                variantRepository.findAll().get(0),
                1,
                new BigDecimal("10.00"),
                "Envío Estándar",
                "Carlos Ruiz",
                "Calle Real 456",
                "Rosario",
                "Santa Fe",
                "S2000",
                "Argentina");
        Long saleId = sale.getId();

        mockMvc.perform(post("/sales/" + saleId + "/canceled")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELED"));

        Sale updatedSale = saleRepository.findById(saleId).orElseThrow();
        assertEquals(EntityStatus.ACTIVE, updatedSale.getUser().getStatus());
        assertEquals(SaleStatus.CANCELED, updatedSale.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testIntentarCancelarVentaYaCancelada() throws Exception {
        Sale sale = createSale(
                userRepository.findAll().get(0),
                LocalDateTime.now(),
                variantRepository.findAll().get(0),
                1,
                new BigDecimal("10.00"),
                "Envío Estándar",
                "Ana Gómez",
                "Avenida Central 789",
                "La Plata",
                "Buenos Aires",
                "B3000",
                "Argentina");
        Long saleId = sale.getId();
        sale.setStatus(SaleStatus.CANCELED);
        saleRepository.save(sale);

        mockMvc.perform(post("/sales/" + saleId + "/canceled")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("La venta ya se encuentra cancelada."));

        Sale updatedSale = saleRepository.findById(saleId).orElseThrow();
        assertEquals(SaleStatus.CANCELED, updatedSale.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testIntentarCancelarVentaCompletada() throws Exception {
        Sale sale = createSale(
                userRepository.findAll().get(0),
                LocalDateTime.now(),
                variantRepository.findAll().get(0),
                1,
                new BigDecimal("10.00"),
                "Envío Estándar",
                "Luis Martínez",
                "Boulevard Libertador 101",
                "Mendoza",
                "Mendoza",
                "M4000",
                "Argentina");
        Long saleId = sale.getId();
        sale.setStatus(SaleStatus.COMPLETED);
        saleRepository.save(sale);

        mockMvc.perform(post("/sales/" + saleId + "/canceled")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("La venta ya se encuentra completada."));

        Sale updatedSale = saleRepository.findById(saleId).orElseThrow();
        assertEquals(SaleStatus.COMPLETED, updatedSale.getStatus());
    }

    // Tests 2.4.3
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testMarcarVentaComoEnviadaEnEstadoInProcess() throws Exception {
        Sale sale = createSale(
                userRepository.findAll().get(0),
                LocalDateTime.now(),
                variantRepository.findAll().get(0),
                1,
                new BigDecimal("10.00"),
                "Envío Estándar",
                "Carlos Ruiz",
                "Calle Real 456",
                "Rosario",
                "Santa Fe",
                "S2000",
                "Argentina");
        Long saleId = sale.getId();

        mockMvc.perform(post("/sales/" + saleId + "/sent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SENT"));

        Sale updatedSale = saleRepository.findById(saleId).orElseThrow();
        assertEquals(SaleStatus.SENT, updatedSale.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testIntentarMarcarComoEnviadaVentaYaEnviada() throws Exception {
        Sale sale = createSale(
                userRepository.findAll().get(0),
                LocalDateTime.now(),
                variantRepository.findAll().get(0),
                1,
                new BigDecimal("10.00"),
                "Envío Estándar",
                "Ana Gómez",
                "Avenida Central 789",
                "La Plata",
                "Buenos Aires",
                "B3000",
                "Argentina");
        Long saleId = sale.getId();
        sale.setStatus(SaleStatus.SENT);
        saleRepository.save(sale);

        mockMvc.perform(post("/sales/" + saleId + "/sent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message").value("Solo las ventas en proceso pueden ser marcadas como enviadas."));

        Sale updatedSale = saleRepository.findById(saleId).orElseThrow();
        assertEquals(SaleStatus.SENT, updatedSale.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testIntentarMarcarComoEnviadaVentaCancelada() throws Exception {
        Sale sale = createSale(
                userRepository.findAll().get(0),
                LocalDateTime.now(),
                variantRepository.findAll().get(0),
                1,
                new BigDecimal("10.00"),
                "Envío Estándar",
                "Luis Martínez",
                "Boulevard Libertador 101",
                "Mendoza",
                "Mendoza",
                "M4000",
                "Argentina");
        Long saleId = sale.getId();
        sale.setStatus(SaleStatus.CANCELED);
        saleRepository.save(sale);

        mockMvc.perform(post("/sales/" + saleId + "/sent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message").value("Solo las ventas en proceso pueden ser marcadas como enviadas."));

        Sale updatedSale = saleRepository.findById(saleId).orElseThrow();
        assertEquals(SaleStatus.CANCELED, updatedSale.getStatus());
    }

    // Tests 2.4.4

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testMarcarVentaComoCompletadaEnEstadoSent() throws Exception {
        Sale sale = createSale(
                userRepository.findAll().get(0),
                LocalDateTime.now(),
                variantRepository.findAll().get(0),
                1,
                new BigDecimal("10.00"),
                "Envío Estándar",
                "Carlos Ruiz",
                "Calle Real 456",
                "Rosario",
                "Santa Fe",
                "S2000",
                "Argentina");
        sale.setStatus(SaleStatus.SENT);
        saleRepository.save(sale);
        Long saleId = sale.getId();

        mockMvc.perform(post("/sales/" + saleId + "/completed")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        Sale updatedSale = saleRepository.findById(saleId).orElseThrow();
        assertEquals(SaleStatus.COMPLETED, updatedSale.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testIntentarCompletarVentaYaCompletada() throws Exception {
        Sale sale = createSale(
                userRepository.findAll().get(0),
                LocalDateTime.now(),
                variantRepository.findAll().get(0),
                1,
                new BigDecimal("10.00"),
                "Envío Estándar",
                "Ana Gómez",
                "Avenida Central 789",
                "La Plata",
                "Buenos Aires",
                "B3000",
                "Argentina");
        sale.setStatus(SaleStatus.COMPLETED);
        saleRepository.save(sale);
        Long saleId = sale.getId();

        mockMvc.perform(post("/sales/" + saleId + "/completed")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message").value("Solo las ventas enviadas pueden ser marcadas como completadas."));

        Sale updatedSale = saleRepository.findById(saleId).orElseThrow();
        assertEquals(SaleStatus.COMPLETED, updatedSale.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testIntentarCompletarVentaCancelada() throws Exception {
        Sale sale = createSale(
                userRepository.findAll().get(0),
                LocalDateTime.now(),
                variantRepository.findAll().get(0),
                1,
                new BigDecimal("10.00"),
                "Envío Estándar",
                "Luis Martínez",
                "Boulevard Libertador 101",
                "Mendoza",
                "Mendoza",
                "M4000",
                "Argentina");
        sale.setStatus(SaleStatus.CANCELED);
        saleRepository.save(sale);
        Long saleId = sale.getId();

        mockMvc.perform(post("/sales/" + saleId + "/completed")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message").value("Solo las ventas enviadas pueden ser marcadas como completadas."));

        Sale updatedSale = saleRepository.findById(saleId).orElseThrow();
        assertEquals(SaleStatus.CANCELED, updatedSale.getStatus());
    }

    // Tests 2.5.4
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testGenerarReporteVentasConRangoDeFechasValido() throws Exception {
        String startDate = "2024-01-01";
        String endDate = "2024-12-31";

        mockMvc.perform(get("/sales/reports")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOrders").value(2))
                .andExpect(jsonPath("$.totalSales").value(315.00))
                .andExpect(jsonPath("$.topProducts.Zapatillas").value(3))
                .andExpect(jsonPath("$.averageOrderValue").value(157.50));
    }
}
