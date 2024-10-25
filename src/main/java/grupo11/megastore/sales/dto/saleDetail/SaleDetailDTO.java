package grupo11.megastore.sales.dto.saleDetail;

import grupo11.megastore.products.dto.variant.VariantDTO;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaleDetailDTO {
    private Long id;
    private VariantDTO variant;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
