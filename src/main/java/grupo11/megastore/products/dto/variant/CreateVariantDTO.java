package grupo11.megastore.products.dto.variant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateVariantDTO {
    @NotNull(message = "El producto es obligatorio")
    private Long productId;

    @Size(min = 1, max = 32, message = "El color debe tener entre 1 y 32 caracteres")
    @NotNull(message = "El color es obligatorio")
    private String color;

    @Size(min = 1, max = 32, message = "El talle debe tener entre 1 y 10 caracteres")
    @NotNull(message = "El talle es obligatorio")
    private String size;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @NotNull(message = "El stock es obligatorio")
    private Integer stock;

    private byte[] image;
}
