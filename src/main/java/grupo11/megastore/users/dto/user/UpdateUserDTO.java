package grupo11.megastore.users.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateUserDTO {
    
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "El nombre solo puede contener letras")
    private String firstName;

    @Size(min = 2, max = 20, message = "El apellido debe tener entre 2 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "El apellido solo puede contener letras")
    private String lastName;

    public boolean isEmpty() {
        return this.firstName == null && this.lastName == null;
    }
}
