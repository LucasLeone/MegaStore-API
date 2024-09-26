package grupo11.megastore.products.dto.variant;

import lombok.Data;

@Data
public class VariantDTO {
    private Long id;
    private Long productId;
    private String color;
    private String size;
    private Integer stock;
    private byte[] image;
}
