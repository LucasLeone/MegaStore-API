package grupo11.megastore.products.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import grupo11.megastore.products.validation.CategoryExists;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateProductDTO {
    @Size(min = 2, max = 32, message = "El nombre debe tener entre 2 y 32 caracteres")
    @NotEmpty(message = "El nombre es obligatorio")
    private String name;

    @Size(max = 128, message = "La descripción no puede exceder 128 caracteres")
    private String description;

    @Positive(message = "El precio debe ser positivo")
    @NotNull(message = "El precio es obligatorio")
    private Double price;

    @NotNull(message = "La categoría es obligatoria")
    @CategoryExists(message = "La categoría no existe")
    private Long categoryId;

    @NotNull(message = "La subcategoría es obligatoria")
    private Long subcategoryId;

    @NotNull(message = "La marca es obligatoria")
    private Long brandId;
}
