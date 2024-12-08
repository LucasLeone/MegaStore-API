package grupo11.megastore.users.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import grupo11.megastore.users.dto.address.CreateAddressDTO;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterUserDTO {

    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "El nombre solo puede contener letras")
    private String firstName;

    @Size(min = 2, max = 20, message = "El apellido debe tener entre 2 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "El apellido solo puede contener letras")
    private String lastName;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "El email debe ser válido")
    private String email;

    @Pattern(regexp = "^[0-9]{9,15}$", message = "El número de teléfono debe tener entre 9 y 15 dígitos")
    private String phoneNumber;

    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    private String password;

    @Size(min = 8, max = 20, message = "La confirmación de la contraseña debe tener entre 8 y 20 caracteres")
    private String passwordConfirmation;

    private CreateAddressDTO address;

    public boolean isEmpty() {
        return this.firstName == null && this.lastName == null && this.email == null && this.password == null
                && this.passwordConfirmation == null && this.phoneNumber == null && this.address == null;
    }
}
