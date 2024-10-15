package grupo11.megastore.carts.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import grupo11.megastore.carts.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser_EmailAndCartId(String email, Long cartId);

    List<Cart> findByCartItemsVariantProductId(Long productId);
}
