package grupo11.megastore.products.interfaces;

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
     * Retrieves all active products with optional filters.
     *
     * @param categoryId    (Optional) The ID of the category to filter by.
     * @param subcategoryId (Optional) The ID of the subcategory to filter by.
     * @param brandId       (Optional) The ID of the brand to filter by.
     * @param name          (Optional) A string to filter products by name containing this value.
     * @return A list of ProductDTO objects.
     */
    List<ProductDTO> getAllProducts(Long categoryId, Long subcategoryId, Long brandId, String name);

    /**
     * Retrieves all deleted products.
     *
     * @return A list of ProductDTO objects.
     */
    List<ProductDTO> getAllDeletedProducts();

    /**
     * Retrieves a specific product by its unique identifier.
     *
     * @param id The unique identifier of the product to retrieve.
     * @return The ProductDTO if found.
     */
    ProductDTO getProductById(Long id);

    /**
     * Creates a new product.
     *
     * @param product The CreateProductDTO containing the details of the product to be created.
     * @return The created ProductDTO.
     */
    ProductDTO createProduct(CreateProductDTO product);

    /**
     * Updates an existing product.
     *
     * @param id      The unique identifier of the product to update.
     * @param product The UpdateProductDTO containing the updated details of the product.
     * @return The updated ProductDTO.
     */
    ProductDTO updateProduct(Long id, UpdateProductDTO product);

    /**
     * Deletes a product by marking its status as DELETED.
     *
     * @param id The unique identifier of the product to delete.
     */
    void deleteProduct(Long id);
}
