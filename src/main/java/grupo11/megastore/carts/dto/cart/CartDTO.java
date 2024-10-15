package grupo11.megastore.carts.dto.cart;

import java.util.List;

import grupo11.megastore.carts.dto.cartItems.CartItemDTO;
import lombok.Data;

@Data
public class CartDTO {
    private Long cartId;
    private String userEmail;
    private double total;
    private List<CartItemDTO> cartItems;
}
