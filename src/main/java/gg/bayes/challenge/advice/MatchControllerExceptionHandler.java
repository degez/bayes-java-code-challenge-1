package gg.bayes.challenge.advice;

import gg.bayes.challenge.exception.*;
import gg.bayes.challenge.rest.model.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ResponseBody
@ControllerAdvice
public class MatchControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    (value = {IllegalArgumentException.class, IllegalStateException.class, ConstraintViolationException.class,
            HeroSpellsNotFoundException.class, HeroDamageNotFoundException.class, HeroItemsNotFoundException.class,
            HeroNotFoundException.class, InvalidTimeFormatException.class})
    protected ResponseEntity<Object> handleInvalidDurationException(RuntimeException ex, WebRequest request) {

        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, details);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);

        return handleExceptionInternal(ex, errorResponse, headers, errorResponse.getHttpStatus(), request);
    }

}
