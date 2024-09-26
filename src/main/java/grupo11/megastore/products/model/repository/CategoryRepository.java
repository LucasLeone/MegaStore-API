package grupo11.megastore.products.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.products.model.Category;

/**
 * Repository interface for managing Category entities.
 * Extends JpaRepository to provide basic CRUD operations and custom query
 * methods.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Retrieves a category by its unique identifier and status.
     * 
     * @param id     The unique identifier of the category to retrieve.
     * @param status The status of the category to filter by (e.g., ACTIVE,
     *               DELETED).
     * @return An Optional containing the found Category if it exists and matches
     *         the status, or empty otherwise.
     */
    Optional<Category> findByIdAndStatus(Long id, EntityStatus status);

    /**
     * Retrieves a category by its name and status.
     * 
     * @param name   The unique name of the category to retrieve.
     * @param status The status of the category to filter by (e.g., ACTIVE,
     *               DELETED).
     * @return An Optional containing the found Category if it exists and matches
     *         the status, or empty otherwise.
     */
    Optional<Category> findByNameAndStatus(String name, EntityStatus status);

    /**
     * Retrieves a category by its name and status, excluding the category with the
     * specified ID.
     * 
     * @param name   The unique name of the category to retrieve.
     * @param status The status of the category to filter by (e.g., ACTIVE,
     *               DELETED).
     * @param id     The unique identifier of the category to exclude from the
     *               search.
     * @return An Optional containing the found Category if it exists and matches
     *         the status, or empty otherwise.
     */
    Optional<Category> findByNameAndStatusAndIdNot(String name, EntityStatus status, Long id);

    /**
     * Retrieves all categories that match the specified status.
     * 
     * @param status The status to filter categories by (e.g., ACTIVE, DELETED).
     * @return A list of Category entities that have the specified status.
     */
    List<Category> findByStatus(EntityStatus status);
}
