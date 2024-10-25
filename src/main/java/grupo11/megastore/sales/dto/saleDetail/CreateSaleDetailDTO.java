package grupo11.megastore.sales.dto.saleDetail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSaleDetailDTO {

    @NotNull(message = "El ID de la variante no puede ser nulo")
    private Long variantId;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer quantity;
}
