package grupo11.megastore.carts.dto.cartItems;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateItemRequest {
    @NotNull(message = "La cantidad es obligatoria")
    private Integer quantity;
}
