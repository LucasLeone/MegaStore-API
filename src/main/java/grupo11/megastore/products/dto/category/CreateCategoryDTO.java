package grupo11.megastore.products.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCategoryDTO {
    @Size(min = 2, max = 32, message = "Name must be between 2 and 32 characters")
    @NotEmpty(message = "El nombre es requerido")
    private String name;

    @Size(max = 128, message = "La descripción debe tener menos de 128 caracteres")
    private String description;
}
