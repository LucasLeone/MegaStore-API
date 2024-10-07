package grupo11.megastore.products.interfaces;

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
     * @return A list of SubcategoryDTO objects.
     */
    public List<SubcategoryDTO> getAllSubcategories(Long categoryId);

    /**
     * Retrieves all deleted subcategories.
     *
     * @return A list of SubcategoryDTO objects.
     */
    public List<SubcategoryDTO> getAllDeletedSubcategories();

    /**
     * Retrieves a specific subcategory by its unique identifier.
     *
     * @param id The unique identifier of the subcategory to retrieve.
     * @return The SubcategoryDTO if found.
     */
    public SubcategoryDTO getSubcategoryById(Long id);

    /**
     * Creates a new subcategory.
     *
     * @param subcategory The CreateSubcategoryDTO containing the details of the subcategory to be created.
     * @return The created SubcategoryDTO.
     */
    public SubcategoryDTO createSubcategory(CreateSubcategoryDTO subcategory);

    /**
     * Updates an existing subcategory.
     *
     * @param id The unique identifier of the subcategory to update.
     * @param subcategory The UpdateSubcategoryDTO containing the updated details of the subcategory.
     * @return The updated SubcategoryDTO.
     */
    public SubcategoryDTO updateSubcategory(Long id, UpdateSubcategoryDTO subcategory);

    /**
     * Deletes a subcategory by marking its status as DELETED.
     *
     * @param id The unique identifier of the subcategory to delete.
     */
    public void deleteSubcategory(Long id);
}
