package grupo11.megastore.users.dto.user;

import lombok.Data;

import grupo11.megastore.users.model.Role;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDTO {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<Role> roles = new HashSet<>();
}
