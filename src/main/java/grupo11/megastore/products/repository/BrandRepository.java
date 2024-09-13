package grupo11.megastore.products.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import grupo11.megastore.products.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    List<Brand> findByStatus(int status);
}
