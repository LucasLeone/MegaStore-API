package grupo11.megastore.products.interfaces;

import org.springframework.http.ResponseEntity;

import grupo11.megastore.products.dto.variant.VariantDTO;
import grupo11.megastore.products.dto.variant.CreateVariantDTO;
import grupo11.megastore.products.dto.variant.UpdateVariantDTO;

import java.util.List;

/**
 * Service interface for managing Variant entities.
 * Defines methods for CRUD operations and other business logic related to variants.
 */
public interface IVariantService {

    /**
     * Retrieves all variants with optional filters.
     *
     * @param productId (Optional) The ID of the product to filter by.
     * @param color     (Optional) The color to filter variants by.
     * @param size      (Optional) The size to filter variants by.
     * 
     * @return A ResponseEntity containing a list of VariantDTO objects.
     */
    ResponseEntity<List<VariantDTO>> getAllVariants(Long productId, String color, String size);

    /**
     * Retrieves a specific variant by its unique identifier.
     *
     * @param id The unique identifier of the variant to retrieve.
     * 
     * @return A ResponseEntity containing the VariantDTO if found, or an appropriate HTTP status otherwise.
     */
    ResponseEntity<VariantDTO> getVariantById(Long id);

    /**
     * Creates a new variant.
     *
     * @param variant The CreateVariantDTO containing the details of the variant to be created.
     
     * @return A ResponseEntity containing the created VariantDTO along with an appropriate HTTP status.
     */
    ResponseEntity<VariantDTO> createVariant(CreateVariantDTO variant);

    /**
     * Updates an existing variant.
     *
     * @param id      The unique identifier of the variant to update.
     * @param variant The UpdateVariantDTO containing the updated details of the variant.
     
     * @return A ResponseEntity containing the updated VariantDTO if the update is successful, or an appropriate HTTP status otherwise.
     */
    ResponseEntity<VariantDTO> updateVariant(Long id, UpdateVariantDTO variant);

    /**
     * Deletes a variant by marking its status as DELETED.
     *
     * @param id The unique identifier of the variant to delete.
     */
    void deleteVariant(Long id);
}
