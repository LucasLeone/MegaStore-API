package grupo11.megastore.products.dto.variant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateVariantDTO {
    @NotNull(message = "El producto es obligatorio")
    private Long productId;

    @NotBlank(message = "El color es obligatorio")
    @Size(max = 32, message = "El color no puede exceder los 32 caracteres")
    private String color;

    @NotBlank(message = "El talle es obligatorio")
    @Size(min = 1, max = 10, message = "El talle debe tener entre 1 y 10 caracteres")
    private String size;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @NotNull(message = "El stock es obligatorio")
    private Integer stock;

    private byte[] image;
}
