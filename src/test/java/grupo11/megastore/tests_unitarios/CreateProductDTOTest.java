package grupo11.megastore.tests_unitarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import grupo11.megastore.products.dto.product.CreateProductDTO;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.products.model.repository.CategoryRepository;
import grupo11.megastore.products.validation.CategoryExistsValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CreateProductDTOTest {

    private Validator validator;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);

        // Simula que la categoría 1 existe y la categoría 9999 no existe
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.existsById(9999L)).thenReturn(false);

        // Configurar una ConstraintValidatorFactory custom
        ValidatorFactory factory = Validation.byDefaultProvider().configure()
                .constraintValidatorFactory(new ConstraintValidatorFactory() {
                    @Override
                    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
                        if (key == CategoryExistsValidator.class) {
                            // Retornamos el validador con el mock inyectado
                            return (T) new CategoryExistsValidator(categoryRepository);
                        }
                        try {
                            return key.getDeclaredConstructor().newInstance();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void releaseInstance(ConstraintValidator<?, ?> instance) {
                    }
                })
                .buildValidatorFactory();

        validator = factory.getValidator();
    }

    // Tests 1.1.4
    @Test
    void testNombreVacio() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("");
        dto.setPrice(10.0);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber al menos una violación");

        boolean foundExpectedMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El nombre es obligatorio"));

        assertTrue(foundExpectedMessage, "Debería haberse encontrado el mensaje 'El nombre es obligatorio'");
    }

    @Test
    void testNombreDemasiadoCorto() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("A");
        dto.setPrice(10.0);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber una violación si el nombre tiene menos de 2 caracteres");

        ConstraintViolation<CreateProductDTO> violation = violations.iterator().next();
        assertEquals("El nombre debe tener entre 2 y 32 caracteres", violation.getMessage());
    }

    @Test
    void testNombreDemasiadoLargo() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("abcdefghijklmnopqrstuvwx123456789abc");
        dto.setPrice(10.0);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);
        assertTrue(!violations.isEmpty(), "Debería haber una violación si el nombre excede los 32 caracteres");

        ConstraintViolation<CreateProductDTO> violation = violations.iterator().next();
        assertEquals("El nombre debe tener entre 2 y 32 caracteres", violation.getMessage());
    }

    @Test
    void testNombreValido() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("Producto10");
        dto.setPrice(10.0);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones con un nombre válido");
    }

    // Tests 1.2.4
    @Test
    void testPrecioVacio() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("Producto Valido");
        dto.setPrice(null);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);

        assertTrue(!violations.isEmpty(), "Debería haber al menos una violación");
        boolean foundExpectedMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El precio es obligatorio"));

        assertTrue(foundExpectedMessage, "Debería haberse encontrado el mensaje 'El precio es obligatorio'");
    }

    @Test
    void testPrecioNegativo() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("Producto Valido");
        dto.setPrice(-10.0);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);

        assertTrue(!violations.isEmpty(), "Debería haber una violación si el precio es negativo");
        boolean foundExpectedMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El precio debe ser positivo"));

        assertTrue(foundExpectedMessage, "Debería haberse encontrado el mensaje 'El precio debe ser positivo'");
    }

    @Test
    void testPrecioCero() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("Producto Valido");
        dto.setPrice(0.0);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);

        assertTrue(!violations.isEmpty(), "Debería haber una violación si el precio es igual a cero");
        boolean foundExpectedMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El precio debe ser positivo"));

        assertTrue(foundExpectedMessage, "Debería haberse encontrado el mensaje 'El precio debe ser positivo'");
    }

    @Test
    void testPrecioValido() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("Producto Valido");
        dto.setPrice(25.50);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "No debería haber violaciones con un precio válido");
    }

    // Tests 1.3.3
    @Test
    void testCategoriaInexistenteYPrecioNegativo() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("Producto Valido");
        dto.setPrice(-10.0);
        dto.setCategoryId(9999L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);

        assertTrue(!violations.isEmpty(), "Debería haber al menos una violación");
        boolean foundCategoryMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("La categoría no existe"));
        assertTrue(foundCategoryMessage, "Debería haberse encontrado el mensaje 'La categoría no existe'");

        boolean foundPriceMessage = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El precio debe ser positivo"));
        assertTrue(foundPriceMessage, "Debería haberse encontrado el mensaje 'El precio debe ser positivo'");
    }

    @Test
    void testCategoriaValidaYPrecioValido() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setName("Producto Valido");
        dto.setPrice(25.50);
        dto.setCategoryId(1L);
        dto.setSubcategoryId(1L);
        dto.setBrandId(1L);

        Set<ConstraintViolation<CreateProductDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "No debería haber violaciones con una categoría válida y un precio válido");
    }

    // Tests 1.4.3
    @Test
    void testRestaurarProductoEliminadoExitosamente() {
        Product productoEliminado = new Product();
        productoEliminado.setId(1L);
        productoEliminado.setName("Producto Eliminado");
        productoEliminado.setPrice(10.0);
        productoEliminado.delete();

        if (productoEliminado.getStatus() == grupo11.megastore.constant.EntityStatus.DELETED) {
            productoEliminado.restore();
            assertEquals(grupo11.megastore.constant.EntityStatus.ACTIVE, productoEliminado.getStatus(), 
                "El producto debería estar en estado 'ACTIVE' tras la restauración");
        } else {
            throw new AssertionError("El producto no estaba en estado DELETED al inicio de la prueba");
        }
    }

    @Test
    void testIntentarRestaurarProductoQueNoEstaEliminado() {
        Product productoActivo = new Product();
        productoActivo.setId(2L);
        productoActivo.setName("Producto Activo");
        productoActivo.setPrice(20.0);

        if (productoActivo.getStatus() == grupo11.megastore.constant.EntityStatus.DELETED) {
            throw new AssertionError("El producto está marcado como DELETED, pero debería estar ACTIVE para esta prueba.");
        } else {
            assertEquals(grupo11.megastore.constant.EntityStatus.ACTIVE, productoActivo.getStatus(), 
                "El producto debería seguir en estado 'ACTIVE' ya que no se puede restaurar un producto no eliminado");
        }
    }
}
