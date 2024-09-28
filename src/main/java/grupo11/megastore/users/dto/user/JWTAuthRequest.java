package grupo11.megastore.users.dto.user;

import lombok.Data;

@Data
public class JWTAuthRequest {
    
    private String username;
    private String password;
}
