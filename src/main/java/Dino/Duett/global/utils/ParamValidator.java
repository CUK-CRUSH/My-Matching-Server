package Dino.Duett.global.utils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ParamValidator {
    public void stringValidator(String param, String paramName) {
        if (param == null || param.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, paramName + " is null or blank");
        }
    }
}
