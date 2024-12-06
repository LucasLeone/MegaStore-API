package grupo11.megastore.products.dto.brand;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBrandDTO {
    @Size(min = 3, max = 32, message = "El nombre debe tener entre 3 y 32 caracteres")
    @NotEmpty(message = "El nombre es requerido")
    private String name;

    @Size(max = 128, message = "La descripción debe tener menos de 128 caracteres")
    private String description;
}
