package grupo11.megastore.products.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProductDTO {
    @Size(min = 2, max = 32, message = "El nombre debe tener entre 2 y 32 caracteres")
    private String name;

    @Size(max = 128, message = "La descripción no puede exceder 128 caracteres")
    private String description;

    @Positive(message = "El precio debe ser positivo")
    private Double price;

    private Long categoryId;

    private Long subcategoryId;

    private Long brandId;

    public boolean isEmpty() {
        return this.name == null && this.description == null && this.price == null && this.categoryId == null
                && this.subcategoryId == null && this.brandId == null;
    }
}
