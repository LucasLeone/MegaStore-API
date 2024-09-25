package grupo11.megastore.products.dto.subcategory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSubcategoryDTO {
    @Size(min = 2, max = 32, message = "El nombre debe tener entre 2 y 32 caracteres")
    @NotEmpty(message = "El nombre es requerido")
    private String name;

    @Size(max = 128, message = "La descripción debe tener menos de 128 caracteres")
    private String description;

    @NotNull(message = "La categoría es requerida")
    private Long categoryId;
}
