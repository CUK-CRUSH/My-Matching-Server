package Dino.Duett.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final int METHOD_ARGUMENT_FIRST_ERROR_INDEX = 0;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleRequestValidationException(
        final MethodArgumentNotValidException e
    ) {
        final FieldError firstErrorField = e.getFieldErrors()
            .get(METHOD_ARGUMENT_FIRST_ERROR_INDEX);
        final CustomException exception = CustomException.fromFieldError(firstErrorField);

        log.error(exception.getErrorInfoLog());

        return ResponseEntity.badRequest().body(ErrorResponse.from(exception));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleEmptyPathVariableException(
        final MissingPathVariableException e
    ) {
        final CustomException exception = CustomException.of(
            ErrorCode.WRONG_REQUEST_URL,
            Map.of(e.getVariableName(), "")
        );

        log.error(exception.getErrorInfoLog());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(exception));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServer(final Exception e) {
        final CustomException customException = CustomException.from(
            ErrorCode.INTERNAL_SERVER_ERROR);

        log.error(e.toString());

        return ResponseEntity.internalServerError().body(ErrorResponse.from(customException));
    }
}
