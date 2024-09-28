package grupo11.megastore.users.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import grupo11.megastore.users.dto.user.LoginCredentials;
import grupo11.megastore.users.dto.user.RegisterUserDTO;
import grupo11.megastore.users.interfaces.IAuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerHandler(@Valid @RequestBody RegisterUserDTO user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginHandler(@Valid @RequestBody LoginCredentials credentials) {
        return authService.login(credentials);
    }
}
