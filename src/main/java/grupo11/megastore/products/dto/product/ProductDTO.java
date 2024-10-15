package grupo11.megastore.products.dto.product;

import grupo11.megastore.products.dto.brand.BrandDTO;
import grupo11.megastore.products.dto.category.CategoryDTO;
import grupo11.megastore.products.dto.subcategory.SubcategoryDTO;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private CategoryDTO category;
    private SubcategoryDTO subcategory;
    private BrandDTO brand;
}
