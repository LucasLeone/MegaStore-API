package grupo11.megastore.products.dto.subcategory;

import grupo11.megastore.products.dto.category.CategoryDTO;
import lombok.Data;

@Data
public class SubcategoryDTO {
    private Long id;
    private String name;
    private String description;
    private CategoryDTO category;
}
