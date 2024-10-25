package grupo11.megastore.sales.dto.sale;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ShippingInfoDTO {
    
    @NotEmpty(message = "El nombre completo es obligatorio")
    private String fullName;
    
    @NotEmpty(message = "La dirección es obligatoria")
    private String address;
    
    @NotEmpty(message = "La ciudad es obligatoria")
    private String city;
    
    @NotEmpty(message = "El código postal es obligatorio")
    private String postalCode;
    
    @NotEmpty(message = "El país es obligatorio")
    private String country;
}
