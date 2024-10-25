package grupo11.megastore.sales.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.sales.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findByIdAndStatus(Long id, EntityStatus status);
    List<Sale> findByStatus(EntityStatus status);
    List<Sale> findByUserIdAndStatus(Long userId, EntityStatus status);
}
