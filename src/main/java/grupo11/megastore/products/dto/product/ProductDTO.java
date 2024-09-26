package grupo11.megastore.products.dto.product;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long categoryId;
    private Long subcategoryId;
    private Long brandId;
}
