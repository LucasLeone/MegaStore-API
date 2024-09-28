package grupo11.megastore.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ErrorResponseBuilder {

    public static ErrorResponse buildErrorResponse(HttpStatus status, String message, String path) {
        ErrorResponse body = new ErrorResponse();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(status.value());
        body.setError(status.getReasonPhrase());
        body.setMessage(message);
        body.setPath(path);
        return body;
    }
}
