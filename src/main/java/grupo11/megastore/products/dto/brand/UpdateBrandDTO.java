package grupo11.megastore.products.dto.brand;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateBrandDTO {
    @Size(min = 2, max = 32, message = "El nombre debe tener entre 2 y 32 caracteres")
    private String name;

    @Size(max = 128, message = "La descripción debe tener menos de 128 caracteres")
    private String description;

    public boolean isEmpty() {
        return this.name == null && this.description == null;
    }
}
