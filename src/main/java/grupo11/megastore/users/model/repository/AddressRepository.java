package grupo11.megastore.users.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import grupo11.megastore.users.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
