package grupo11.megastore.users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 50, message = "La calle no puede tener más de 50 caracteres")
    private String street;

    @NotBlank(message = "El número es obligatorio")
    @Size(max = 10, message = "El número no puede tener más de 10 caracteres")
    private String number;

    @Size(max = 10, message = "El piso no puede tener más de 10 caracteres")
    private String floor;

    @Size(max = 10, message = "El departamento no puede tener más de 10 caracteres")
    private String apartment;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 50, message = "La ciudad no puede tener más de 50 caracteres")
    private String city;

    @NotBlank(message = "La provincia es obligatoria")
    @Size(max = 50, message = "La provincia no puede tener más de 50 caracteres")
    private String state;

    @NotBlank(message = "El código postal es obligatorio")
    @Size(max = 10, message = "El código postal no puede tener más de 10 caracteres")
    private String postalCode;

    @NotBlank(message = "El país es obligatorio")
    @Size(max = 50, message = "El país no puede tener más de 50 caracteres")
    private String country;
}
