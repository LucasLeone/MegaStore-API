package grupo11.megastore.users.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import grupo11.megastore.security.JWTUtil;
import grupo11.megastore.users.dto.user.LoginCredentials;
import grupo11.megastore.users.dto.user.RegisterUserDTO;
import grupo11.megastore.users.dto.user.UserDTO;
import grupo11.megastore.users.interfaces.IAuthService;
import grupo11.megastore.users.interfaces.IUserService;
import grupo11.megastore.exception.BadRequestException;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private IUserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Map<String, Object>> register(RegisterUserDTO body) {
        if (!body.getPassword().equals(body.getPasswordConfirmation())) {
            throw new BadRequestException("Las contraseñas no coinciden");
        }

        String encodedPassword = passwordEncoder.encode(body.getPassword());
        body.setPassword(encodedPassword);
        body.setPasswordConfirmation(encodedPassword);

        this.userService.registerUser(body);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario registrado con éxito");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Map<String, Object>> login(LoginCredentials credentials) {
        try {
            UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
                    credentials.getEmail(), credentials.getPassword());
            this.authenticationManager.authenticate(authCredentials);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(credentials.getEmail());

        UserDTO userDTO = userService.getUserByEmail(credentials.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("jwt-token", token);
        response.put("user", userDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
