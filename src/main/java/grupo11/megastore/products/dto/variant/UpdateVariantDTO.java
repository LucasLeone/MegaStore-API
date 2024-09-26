package grupo11.megastore.products.dto.variant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateVariantDTO {
    private Long productId;

    @Size(min = 1, max = 32, message = "El color debe tener entre 1 y 32 caracteres")
    private String color;

    @Size(min = 1, max = 32, message = "El talle debe tener entre 1 y 10 caracteres")
    private String size;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private byte[] image;

    public boolean isEmpty() {
        return this.productId == null && this.color == null && this.size == null && this.stock == null
                && this.image == null;
    }
}
