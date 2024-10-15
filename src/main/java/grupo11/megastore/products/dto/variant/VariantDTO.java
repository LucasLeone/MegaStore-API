package grupo11.megastore.products.dto.variant;

import lombok.Data;
import grupo11.megastore.products.dto.product.ProductDTO;

@Data
public class VariantDTO {
    private Long id;
    private ProductDTO product;
    private String color;
    private String size;
    private Integer stock;
    private byte[] image;
}
