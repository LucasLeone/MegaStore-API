package grupo11.megastore.users.dto.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressDTO {
    private Long id;
    private String street;
    private String number;
    private String floor;
    private String apartment;
    private String city;
    private String postalCode;
    private String country;
}
