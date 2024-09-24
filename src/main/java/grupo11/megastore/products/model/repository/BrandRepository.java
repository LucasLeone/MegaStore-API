package grupo11.megastore.products.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.products.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    /**
     * Find a brand by id and status
     * 
     * @param id     id of the brand to search
     * @param status status of the brand to search
     * 
     * @return
     */
    Optional<Brand> findByIdAndStatus(Long id, EntityStatus status);

    /**
     * Find a brand by name
     * 
     * @param name   name of the brand to search
     * @param status status of the brand to search
     * 
     * @return
     */
    Optional<Brand> findByNameAndStatus(String name, EntityStatus status);

    /**
     * Find all brands by status
     * 
     * @param status status of the brand to search
     * 
     * @return
     */
    List<Brand> findByStatus(EntityStatus status);
}