package grupo11.megastore.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Manejador personalizado para excepciones de acceso denegado.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Crear una instancia de ForbiddenException con un mensaje personalizado
        ForbiddenException exception = new ForbiddenException("No tienes permiso para realizar esta acción.");

        // Construir la respuesta de error utilizando el ErrorResponseBuilder
        ErrorResponse body = ErrorResponseBuilder.buildErrorResponse(
                HttpStatus.FORBIDDEN,
                exception.getMessage(),
                request.getRequestURI()
        );

        // Configurar la respuesta HTTP
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // Escribir la respuesta JSON en el cuerpo de la respuesta
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
