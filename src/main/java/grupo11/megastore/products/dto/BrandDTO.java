package grupo11.megastore.products.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class BrandDTO {
    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;
    private String description;
}
