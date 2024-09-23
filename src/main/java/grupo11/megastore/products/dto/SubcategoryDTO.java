package grupo11.megastore.products.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubcategoryDTO {
    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private Integer categoryId;
}
