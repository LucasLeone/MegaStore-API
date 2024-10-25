package grupo11.megastore.sales.dto.sale;

import grupo11.megastore.users.dto.user.UserDTO;
import grupo11.megastore.sales.dto.saleDetail.SaleDetailDTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleDTO {
    private Long id;
    private UserDTO user;
    private LocalDateTime saleDate;
    private String paymentMethod;
    private BigDecimal totalAmount;
    private List<SaleDetailDTO> saleDetails;
    private String fullName;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private String status;
}
