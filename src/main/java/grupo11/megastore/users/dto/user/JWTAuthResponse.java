package grupo11.megastore.users.dto.user;

import lombok.Data;

@Data
public class JWTAuthResponse {
    
    private String token;

    private UserDTO user;
}
