package grupo11.megastore.sales.dto.sale;

import grupo11.megastore.sales.dto.saleDetail.CreateSaleDetailDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class CreateSaleDTO {

    @NotNull
    private Long userId;

    @NotNull
    private String paymentMethod;

    @NotEmpty
    private List<@Valid CreateSaleDetailDTO> saleDetails;


    @NotNull
    @Valid
    private ShippingInfoDTO shippingInfo;
}
