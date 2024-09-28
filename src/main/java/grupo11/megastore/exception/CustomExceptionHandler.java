package grupo11.megastore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.bind.MethodArgumentNotValidException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    /**
     * Manejar excepciones de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                                 .stream()
                                 .map(fieldError -> fieldError.getDefaultMessage())
                                 .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                                 .orElse("Error de validación");

        ErrorResponse body = ErrorResponseBuilder.buildErrorResponse(
            HttpStatus.BAD_REQUEST, 
            errorMessage, 
            request.getDescription(false).replace("uri=", "")
        );

        logger.error("Error de validación: {}", errorMessage);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Manejar UnauthorizedException
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        ErrorResponse body = ErrorResponseBuilder.buildErrorResponse(
            HttpStatus.UNAUTHORIZED, 
            ex.getMessage(), 
            request.getDescription(false).replace("uri=", "")
        );

        logger.error("Autenticación fallida: {}", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Manejar ForbiddenException
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        ErrorResponse body = ErrorResponseBuilder.buildErrorResponse(
            HttpStatus.FORBIDDEN, 
            ex.getMessage(), 
            request.getDescription(false).replace("uri=", "")
        );

        logger.error("Acceso denegado: {}", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    /**
     * Manejar UserNotFoundException
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ErrorResponse body = ErrorResponseBuilder.buildErrorResponse(
            HttpStatus.NOT_FOUND, 
            ex.getMessage(), 
            request.getDescription(false).replace("uri=", "")
        );

        logger.error("Usuario no encontrado: {}", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Manejar BadRequestException
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ErrorResponse body = ErrorResponseBuilder.buildErrorResponse(
            HttpStatus.BAD_REQUEST, 
            ex.getMessage(), 
            request.getDescription(false).replace("uri=", "")
        );

        logger.error("Solicitud inválida: {}", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Manejar todas las demás excepciones
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("Error inesperado: ", ex);

        ErrorResponse body = ErrorResponseBuilder.buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Ocurrió un error inesperado.",
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
