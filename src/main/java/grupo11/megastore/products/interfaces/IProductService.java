package grupo11.megastore.products.interfaces;

import org.springframework.http.ResponseEntity;

import grupo11.megastore.products.dto.product.ProductDTO;
import grupo11.megastore.products.dto.product.CreateProductDTO;
import grupo11.megastore.products.dto.product.UpdateProductDTO;

import java.util.List;

/**
 * Service interface for managing Product entities.
 * Defines methods for CRUD operations and other business logic related to products.
 */
public interface IProductService {

    /**
     * Retrieves all products.
     *
     * @return A ResponseEntity containing a list of ProductDTO objects.
     */
    ResponseEntity<List<ProductDTO>> getAllProducts();

    /**
     * Retrieves a specific product by its unique identifier.
     *
     * @param id The unique identifier of the product to retrieve.
     * @return A ResponseEntity containing the ProductDTO if found, or an appropriate HTTP status otherwise.
     */
    ResponseEntity<ProductDTO> getProductById(Long id);

    /**
     * Creates a new product.
     *
     * @param product The CreateProductDTO containing the details of the product to be created.
     * @return A ResponseEntity containing the created ProductDTO along with an appropriate HTTP status.
     */
    ResponseEntity<ProductDTO> createProduct(CreateProductDTO product);

    /**
     * Updates an existing product.
     *
     * @param id      The unique identifier of the product to update.
     * @param product The UpdateProductDTO containing the updated details of the product.
     * @return A ResponseEntity containing the updated ProductDTO if the update is successful, or an appropriate HTTP status otherwise.
     */
    ResponseEntity<ProductDTO> updateProduct(Long id, UpdateProductDTO product);

    /**
     * Deletes a product by marking its status as DELETED.
     *
     * @param id The unique identifier of the product to delete.
     */
    void deleteProduct(Long id);
}
