package grupo11.megastore.products.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCategoryDTO {
    @Size(min = 2, max = 32, message = "Name must be between 2 and 32 characters")
    @NotEmpty(message = "Name is required")
    private String name;

    @Size(max = 128, message = "Description must be less than 128 characters")
    private String description;
}
