package grupo11.megastore.products.dto.subcategory;

import lombok.Data;

@Data
public class SubcategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
}
