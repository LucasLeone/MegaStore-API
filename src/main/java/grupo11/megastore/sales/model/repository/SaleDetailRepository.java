package grupo11.megastore.sales.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import grupo11.megastore.sales.model.SaleDetail;

public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {
}
