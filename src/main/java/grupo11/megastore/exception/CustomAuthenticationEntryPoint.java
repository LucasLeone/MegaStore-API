package grupo11.megastore.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Punto de entrada personalizado para excepciones de autenticación.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Crear una instancia de UnauthorizedException con un mensaje personalizado
        UnauthorizedException exception = new UnauthorizedException("No tienes permisos para acceder a este recurso.");

        // Construir la respuesta de error utilizando el ErrorResponseBuilder
        ErrorResponse body = ErrorResponseBuilder.buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                request.getRequestURI()
        );

        // Configurar la respuesta HTTP
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // Escribir la respuesta JSON en el cuerpo de la respuesta
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
