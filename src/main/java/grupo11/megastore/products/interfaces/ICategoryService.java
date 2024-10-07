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
    public List<CategoryDTO> getAllCategories();


    /**
     * Get all deleted categories
     * 
     * @return
     */
    public List<CategoryDTO> getAllDeletedCategories();

    /**
     * Get a category by id
     * 
     * @param id
     * 
     * @return
     */
    public CategoryDTO getCategoryById(Long id);

    /**
     * Create a new category
     * 
     * @param category
     * 
     * @return
     */
    public CategoryDTO createCategory(CreateCategoryDTO category);

    /**
     * Update a category
     * 
     * @param id
     * @param category
     * 
     * @return
     */
    public CategoryDTO updateCategory(Long id, UpdateCategoryDTO category);

    /**
     * Delete a category
     * 
     * @param id
     * 
     * @return
     */
    public void deleteCategory(Long id);
}
