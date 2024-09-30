package grupo11.megastore.products.interfaces;

import org.springframework.http.ResponseEntity;

import grupo11.megastore.products.dto.subcategory.SubcategoryDTO;
import grupo11.megastore.products.dto.subcategory.CreateSubcategoryDTO;
import grupo11.megastore.products.dto.subcategory.UpdateSubcategoryDTO;

import java.util.List;

/**
 * Service interface for managing Subcategory entities.
 * Defines methods for CRUD operations and other business logic related to subcategories.
 */
public interface ISubcategoryService {

    /**
     * Retrieves all subcategories. If categoryId is provided, filters by that category.
     * 
     * @param categoryId (Opcional) The ID of the category to filter subcategories.
     * @return A ResponseEntity containing a list of SubcategoryDTO objects.
     */
    public ResponseEntity<List<SubcategoryDTO>> getAllSubcategories(Long categoryId);

    /**
     * Retrieves a specific subcategory by its unique identifier.
     * 
     * @param id The unique identifier of the subcategory to retrieve.
     * @return A ResponseEntity containing the SubcategoryDTO if found, or an appropriate HTTP status otherwise.
     */
    public ResponseEntity<SubcategoryDTO> getSubcategoryById(Long id);

    /**
     * Creates a new subcategory.
     * 
     * @param subcategory The CreateSubcategoryDTO containing the details of the subcategory to be created.
     * @return A ResponseEntity containing the created SubcategoryDTO along with an appropriate HTTP status.
     */
    public ResponseEntity<SubcategoryDTO> createSubcategory(CreateSubcategoryDTO subcategory);

    /**
     * Updates an existing subcategory.
     * 
     * @param id The unique identifier of the subcategory to update.
     * @param subcategory The UpdateSubcategoryDTO containing the updated details of the subcategory.
     * @return A ResponseEntity containing the updated SubcategoryDTO if the update is successful, or an appropriate HTTP status otherwise.
     */
    public ResponseEntity<SubcategoryDTO> updateSubcategory(Long id, UpdateSubcategoryDTO subcategory);

    /**
     * Deletes a subcategory by marking its status as DELETED.
     * 
     * @param id The unique identifier of the subcategory to delete.
     */
    public void deleteSubcategory(Long id);
}
