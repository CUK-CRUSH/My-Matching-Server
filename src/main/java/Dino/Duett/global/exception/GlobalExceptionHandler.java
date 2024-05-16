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
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleInternalServer(final Exception e) {
//        final CustomException customException = CustomException.from(
//            ErrorCode.INTERNAL_SERVER_ERROR);
//
//        log.error(e.toString());
//
//        return ResponseEntity.internalServerError().body(ErrorResponse.from(customException));
//    }
}
