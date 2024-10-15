package grupo11.megastore.carts.dto.cartItems;

import grupo11.megastore.products.dto.variant.VariantDTO;
import lombok.Data;

@Data
public class CartItemDTO {
    private Long cartItemId;
	private VariantDTO variant;
	private Integer quantity;
	private double productPrice;
	private double subtotal;
}
