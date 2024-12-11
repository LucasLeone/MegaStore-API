package grupo11.megastore.tests_integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import grupo11.megastore.config.TestConfig;
import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.dto.brand.CreateBrandDTO;
import grupo11.megastore.products.dto.brand.UpdateBrandDTO;
import grupo11.megastore.products.model.Brand;
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

    @Autowired
    private ObjectMapper objectMapper;

    private Brand existingBrand;

    @BeforeEach
    void setup() {
        brandRepository.deleteAll();

        // Crear Marca
        Brand brand = new Brand();
        brand.setName("TestMarca");
        brand.setDescription("Test descripcion");
        this.existingBrand = brandRepository.save(brand);
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

    // Tests 2.2.3
    @Test
    void testDescripcionNula() throws Exception {
        UpdateBrandDTO updateBrandDTO = new UpdateBrandDTO();
        updateBrandDTO.setName("Marca Actualizada");
        updateBrandDTO.setDescription("");

        mockMvc.perform(put("/brands/{id}", existingBrand.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBrandDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Marca Actualizada"))
                .andExpect(jsonPath("$.description").value(""));

        Brand updatedBrand = brandRepository.findById(existingBrand.getId()).orElseThrow();
        assert updatedBrand.getName().equals("Marca Actualizada");
        assert updatedBrand.getDescription() == "";
    }

    @Test
    void testDescripcionValida() throws Exception {
        UpdateBrandDTO updateBrandDTO = new UpdateBrandDTO();
        updateBrandDTO.setName("Marca Actualizada");
        updateBrandDTO.setDescription("Descripción de la marca actualizada");

        mockMvc.perform(put("/brands/{id}", existingBrand.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBrandDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Marca Actualizada"))
                .andExpect(jsonPath("$.description").value("Descripción de la marca actualizada"));

        Brand updatedBrand = brandRepository.findById(existingBrand.getId()).orElseThrow();
        assert updatedBrand.getName().equals("Marca Actualizada");
        assert updatedBrand.getDescription().equals("Descripción de la marca actualizada");
    }

    @Test
    void testDescripcionExcedeMaximo() throws Exception {
        StringBuilder longDescription = new StringBuilder();
        for (int i = 0; i < 130; i++) {
            longDescription.append("a");
        }

        UpdateBrandDTO updateBrandDTO = new UpdateBrandDTO();
        updateBrandDTO.setName("Marca Actualizada");
        updateBrandDTO.setDescription(longDescription.toString());

        mockMvc.perform(put("/brands/{id}", existingBrand.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBrandDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description").value("La descripción debe tener menos de 128 caracteres"));

        Brand updatedBrand = brandRepository.findById(existingBrand.getId()).orElseThrow();
        assert updatedBrand.getName().equals("TestMarca");
        assert updatedBrand.getDescription().equals("Test descripcion");
    }

    // Tests 2.4.1
    @Test
    void testRestaurarMarcaEliminadaCorrectamente() throws Exception {
        Brand deletedBrand = new Brand();
        deletedBrand.setName("MarcaEliminada");
        deletedBrand.setDescription("Descripción eliminada");
        deletedBrand.setStatus(EntityStatus.DELETED);
        brandRepository.save(deletedBrand);
        Long deletedBrandId = deletedBrand.getId();

        mockMvc.perform(post("/brands/" + deletedBrandId + "/restore")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Brand restoredBrand = brandRepository.findById(deletedBrandId).orElseThrow();
        assert restoredBrand.getStatus() == EntityStatus.ACTIVE;
    }

    @Test
    void testRestaurarMarcaYaActiva() throws Exception {
        Long activeBrandId = existingBrand.getId();

        mockMvc.perform(post("/brands/" + activeBrandId + "/restore")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Marca no encontrado con id: " + activeBrandId));

        Brand activeBrand = brandRepository.findById(activeBrandId).orElseThrow();
        assert activeBrand.getStatus() == EntityStatus.ACTIVE;
    }

    @Test
    void testRestaurarMarcaInexistente() throws Exception {
        Long nonExistentBrandId = 999L;

        mockMvc.perform(post("/brands/" + nonExistentBrandId + "/restore")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Marca no encontrado con id: " + nonExistentBrandId));
    }

    // Tests 2.5.1
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testIntentarCrearMarcaConNombreDuplicado() throws Exception {
        Brand existingBrand = new Brand();
        existingBrand.setName("Electrónica");
        existingBrand.setDescription("Categoría de productos electrónicos");
        existingBrand.setStatus(EntityStatus.ACTIVE);
        brandRepository.save(existingBrand);

        long brandCountBefore = brandRepository.count();

        CreateBrandDTO createBrandDTO = new CreateBrandDTO();
        createBrandDTO.setName("Electrónica");
        createBrandDTO.setDescription("Otra descripción");

        mockMvc.perform(post("/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBrandDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("La marca ya existe"));

        long brandCount = brandRepository.count();
        assertEquals(brandCountBefore, brandCount, "No se debe haber creado una marca duplicada");
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testCrearMarcaConNombreUnico() throws Exception {
        boolean exists = brandRepository.findByNameIgnoreCaseAndStatus("Deportes Extremos", EntityStatus.ACTIVE)
                .isPresent();
        assertEquals(false, exists, "No debe existir una marca con el nombre 'Deportes Extremos'");

        CreateBrandDTO createBrandDTO = new CreateBrandDTO();
        createBrandDTO.setName("Deportes Extremos");
        createBrandDTO.setDescription("Categoría de deportes extremos");

        mockMvc.perform(post("/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBrandDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Deportes Extremos"))
                .andExpect(jsonPath("$.description").value("Categoría de deportes extremos"));

        long brandCount = brandRepository.count();
        assertEquals(2, brandCount, "La marca 'Deportes Extremos' debería haber sido creada");

        Brand createdBrand = brandRepository.findByNameIgnoreCaseAndStatus("Deportes Extremos", EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", "name", "Deportes Extremos"));
        assertEquals("Deportes Extremos", createdBrand.getName());
        assertEquals("Categoría de deportes extremos", createdBrand.getDescription());
        assertEquals(EntityStatus.ACTIVE, createdBrand.getStatus(), "El estado de la marca debería ser ACTIVE");
    }

}
