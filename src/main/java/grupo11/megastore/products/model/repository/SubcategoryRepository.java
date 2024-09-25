package grupo11.megastore.products.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.products.model.Subcategory;

/**
 * Repository interface for managing Subcategory entities.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    
    /**
     * Find a subcategory by its ID and status.
     * 
     * @param id     The ID of the subcategory to search for.
     * @param status The status of the subcategory to filter by.
     * 
     * @return       An Optional containing the subcategory if found.
     */
    Optional<Subcategory> findByIdAndStatus(Long id, EntityStatus status);

    /**
     * Find a subcategory by its name and status.
     * 
     * @param name   The name of the subcategory to search for.
     * @param status The status of the subcategory to filter by.
     * 
     * @return       An Optional containing the subcategory if found.
     */
    Optional<Subcategory> findByNameAndStatus(String name, EntityStatus status);

    /**
     * Find all subcategories by their status.
     * 
     * @param status The status to filter subcategories by.
     * 
     * @return       A list of subcategories matching the specified status.
     */
    List<Subcategory> findByStatus(EntityStatus status);
    
    /**
     * (Optional) Find all subcategories associated with a specific category ID and status.
     * 
     * @param categoryId The ID of the category to which the subcategories belong.
     * @param status     The status to filter subcategories by.
     * @return           A list of subcategories belonging to the specified category and matching the status.
     */
    List<Subcategory> findByCategoryIdAndStatus(Long categoryId, EntityStatus status);
    
    /**
     * (Optional) Find all subcategories associated with a specific category ID.
     * 
     * @param categoryId The ID of the category to which the subcategories belong.
     * @return           A list of subcategories belonging to the specified category.
     */
    List<Subcategory> findByCategoryId(Long categoryId);
}
