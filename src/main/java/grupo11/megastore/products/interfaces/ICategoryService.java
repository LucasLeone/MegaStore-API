package grupo11.megastore.products.interfaces;

import grupo11.megastore.products.dto.category.CategoryDTO;
import grupo11.megastore.products.dto.category.CreateCategoryDTO;
import grupo11.megastore.products.dto.category.UpdateCategoryDTO;

import java.util.List;

public interface ICategoryService {
    /**
     * Get all categories
     * 
     * @return
     */
    List<CategoryDTO> getAllCategories();


    /**
     * Get all deleted categories
     * 
     * @return
     */
    List<CategoryDTO> getAllDeletedCategories();

    /**
     * Get a category by id
     * 
     * @param id
     * 
     * @return
     */
    CategoryDTO getCategoryById(Long id);

    /**
     * Create a new category
     * 
     * @param category
     * 
     * @return
     */
    CategoryDTO createCategory(CreateCategoryDTO category);

    /**
     * Update a category
     * 
     * @param id
     * @param category
     * 
     * @return
     */
    CategoryDTO updateCategory(Long id, UpdateCategoryDTO category);

    /**
     * Delete a category
     * 
     * @param id
     * 
     * @return
     */
    void deleteCategory(Long id);

    /**
     * Restore a category
     * 
     * @param id
     * 
     * @return
     */
    void restoreCategory(Long id);
}
