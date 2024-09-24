package grupo11.megastore.products.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCategoryDTO {
    @Size(min = 2, max = 32, message = "Name must be between 2 and 32 characters")
    private String name;

    @Size(max = 128, message = "Description must be less than 128 characters")
    private String description;

    public boolean isEmpty() {
        return this.name == null && this.description == null;
    }
}
