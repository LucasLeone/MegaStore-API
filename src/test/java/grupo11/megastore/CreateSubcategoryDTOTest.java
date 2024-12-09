package grupo11.megastore;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import grupo11.megastore.products.dto.subcategory.CreateSubcategoryDTO;
import grupo11.megastore.products.model.Subcategory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CreateSubcategoryDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Tests 1.3.4
    @Test
    void testNombreDemasiadoCortoYCategoriaNula() {
        CreateSubcategoryDTO dto = new CreateSubcategoryDTO();
        dto.setName("a");
        dto.setCategoryId(null);

        Set<ConstraintViolation<CreateSubcategoryDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si el nombre tiene menos de 2 caracteres y la categoría es nula");

        boolean hasSizeViolation = violations.stream()
                .anyMatch(v -> "El nombre debe tener entre 2 y 32 caracteres".equals(v.getMessage()));
        assertTrue(hasSizeViolation, "Debe haber una violación con mensaje 'El nombre debe tener entre 2 y 32 caracteres'");
    }

    @Test
    void testCategoriaNula() {
        CreateSubcategoryDTO dto = new CreateSubcategoryDTO();
        dto.setCategoryId(null);

        Set<ConstraintViolation<CreateSubcategoryDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si la categoría es nula");

        boolean hasNotNullViolation = violations.stream()
                .anyMatch(v -> "La categoría es requerida".equals(v.getMessage()));
        assertTrue(hasNotNullViolation, "Debe haber una violación con mensaje 'La categoría es requerida'");
    }

    @Test
    void testNombreDemasiadoCortoYCategoriaInexistente() {
        CreateSubcategoryDTO dto = new CreateSubcategoryDTO();
        dto.setName("a");
        dto.setCategoryId(1L);

        Set<ConstraintViolation<CreateSubcategoryDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber violaciones si el nombre tiene menos de 2 caracteres y la categoría no existe");

        boolean hasSizeViolation = violations.stream()
                .anyMatch(v -> "El nombre debe tener entre 2 y 32 caracteres".equals(v.getMessage()));
        assertTrue(hasSizeViolation, "Debe haber una violación con mensaje 'El nombre debe tener entre 2 y 32 caracteres'");
    }

    @Test
    void testNombreValidoYCategoriaValida() {
        CreateSubcategoryDTO dto = new CreateSubcategoryDTO();
        dto.setName("ab");
        dto.setCategoryId(1L);

        Set<ConstraintViolation<CreateSubcategoryDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones si el nombre tiene 2 caracteres y la categoría existe");
    }

    // Tests 1.4.5
    @Test
    void testEliminarSubcategoriaActivaExitosamente() {
        Subcategory subcategoriaActiva = new Subcategory();
        subcategoriaActiva.setId(1L);
        subcategoriaActiva.setName("Subcategoría Activa");
        
        if (subcategoriaActiva.getStatus() == grupo11.megastore.constant.EntityStatus.ACTIVE) {
            subcategoriaActiva.delete(); 
            org.junit.jupiter.api.Assertions.assertEquals(grupo11.megastore.constant.EntityStatus.DELETED, 
                subcategoriaActiva.getStatus(), "La subcategoría debería estar en estado 'DELETED'");
        } else {
            org.junit.jupiter.api.Assertions.fail("La subcategoría no estaba en estado ACTIVE antes de eliminarla, lo cual no concuerda con el escenario.");
        }
    }

    @Test
    void testIntentarEliminarSubcategoriaYaEliminada() {
        Subcategory subcategoriaEliminada = new Subcategory();
        subcategoriaEliminada.setId(2L);
        subcategoriaEliminada.setName("Subcategoría Eliminada");
        subcategoriaEliminada.delete();

        if (subcategoriaEliminada.getStatus() == grupo11.megastore.constant.EntityStatus.ACTIVE) {
            org.junit.jupiter.api.Assertions.fail("La subcategoría está en estado ACTIVE, se esperaba que estuviera DELETED.");
        } else {
            org.junit.jupiter.api.Assertions.assertEquals(grupo11.megastore.constant.EntityStatus.DELETED, 
                subcategoriaEliminada.getStatus(), "La subcategoría debería mantenerse en estado 'DELETED'");
        }
    }

    // Tests 1.4.1
    @Test
    void testIntentarRestaurarSubcategoriaExitosamente() {
        Subcategory subcategoriaEliminada = new Subcategory();
        subcategoriaEliminada.setId(3L);
        subcategoriaEliminada.setName("Subcategoría Eliminada");
        subcategoriaEliminada.delete();
        subcategoriaEliminada.restore();

        if (subcategoriaEliminada.getStatus() == grupo11.megastore.constant.EntityStatus.ACTIVE) {
            org.junit.jupiter.api.Assertions.assertEquals(grupo11.megastore.constant.EntityStatus.ACTIVE, 
                subcategoriaEliminada.getStatus(), "La subcategoría debería estar en estado 'ACTIVE'");
        } else {
            org.junit.jupiter.api.Assertions.fail("La subcategoría no estaba en estado DELETED antes de restaurarla, lo cual no concuerda con el escenario.");
        }
    }

    @Test
    void testIntentarRestaurarSubcategoriaYaActiva() {
        Subcategory subcategoriaActiva = new Subcategory();
        subcategoriaActiva.setId(4L);
        subcategoriaActiva.setName("Subcategoría Activa");

        if (subcategoriaActiva.getStatus() == grupo11.megastore.constant.EntityStatus.ACTIVE) {
            subcategoriaActiva.restore();
            org.junit.jupiter.api.Assertions.assertEquals(grupo11.megastore.constant.EntityStatus.ACTIVE, 
                subcategoriaActiva.getStatus(), "La subcategoría debería mantenerse en estado 'ACTIVE'");
        } else {
            org.junit.jupiter.api.Assertions.fail("La subcategoría no estaba en estado ACTIVE antes de restaurarla, lo cual no concuerda con el escenario.");
        }
    }
}
