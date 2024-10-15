package grupo11.megastore.carts.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import grupo11.megastore.carts.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartCartIdAndVariantId(Long cartId, Long variantId);

    @Modifying
    @Transactional
    void deleteByCartCartIdAndVariantId(Long cartId, Long variantId);
}