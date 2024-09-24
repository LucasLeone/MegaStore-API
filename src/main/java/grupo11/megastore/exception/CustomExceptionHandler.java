package grupo11.megastore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
/**
 * This class serves as an exception handler for controllers in general.
 *
 * Here, events caused by the controller at a given time can be handled, such as
 * exceptions detected at the controller level (500 Internal Server Error, 400
 * Bad Request, etc.)
 */
public class CustomExceptionHandler {

    /**
     * Helper function that builds the response body for a validation error
     * (Bad Request 400)
     * 
     * @param status
     * @param message
     * @param request
     * @return
     */
    private Map<String, Object> buildBadRequestResponseBody(HttpStatus status, List<String> message,
            WebRequest request) {
        // Create the original Springboot body, adding a new property where the errors
        // are present.
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("details", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return body;
    }

    // Handle validation errors only.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        // Create a list for validation errors.
        List<String> errors = new ArrayList<>();

        // Iterate through each one.
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors())
            errors.add(fieldError.getDefaultMessage());

        // Create the original Springboot body, adding a new property where the errors
        // are present.
        Map<String, Object> body = this.buildBadRequestResponseBody(HttpStatus.BAD_REQUEST, errors, request);

        // Return the response.
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
