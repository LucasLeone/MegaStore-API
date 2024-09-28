package grupo11.megastore.users.interfaces;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import grupo11.megastore.users.dto.user.LoginCredentials;
import grupo11.megastore.users.dto.user.RegisterUserDTO;

public interface IAuthService {
    /**
     * Login method
     * @param credentials
     * @return ResponseEntity<Map<String, Object>>
     */
    ResponseEntity<Map<String, Object>> login(LoginCredentials credentials);

    /**
     * Register method
     * @param userDTO
     * @return ResponseEntity<Map<String, Object>>
     */
    ResponseEntity<Map<String, Object>> register(RegisterUserDTO userDTO);
}
