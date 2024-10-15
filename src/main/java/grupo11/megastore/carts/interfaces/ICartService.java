package grupo11.megastore.carts.interfaces;

import grupo11.megastore.carts.dto.cart.CartDTO;

public interface ICartService {
    CartDTO addProductToCart(Long variantId, Integer quantity);
    CartDTO getMyCart();
    CartDTO updateProductQuantityInCart(Long variantId, Integer quantity);
    void deleteProductFromCart(Long variantId);
}
