package grupo11.megastore.products.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.products.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Find a category by id and status
     * 
     * @param id     id of the cateory to search
     * @param status status of the cateory to search
     * 
     * @return
     */
    Optional<Category> findByIdAndStatus(Long id, EntityStatus status);

    /**
     * Find a category by name
     * 
     * @param name   name of the cateory to search
     * @param status status of the cateory to search
     * 
     * @return
     */
    Optional<Category> findByNameAndStatus(String name, EntityStatus status);

    /**
     * Find all categories by status
     * 
     * @param status status of the cateory to search
     * 
     * @return
     */
    List<Category> findByStatus(EntityStatus status);
}
