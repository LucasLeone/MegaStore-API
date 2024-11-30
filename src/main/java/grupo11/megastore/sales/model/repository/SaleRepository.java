package grupo11.megastore.sales.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import grupo11.megastore.sales.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByUserId(Long userId);
}
