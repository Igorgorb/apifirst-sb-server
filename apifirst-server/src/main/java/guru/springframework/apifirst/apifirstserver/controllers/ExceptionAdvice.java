package guru.springframework.apifirst.apifirstserver.controllers;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(value = HttpStatus.CONFLICT,
            reason = "Data integrity violation") // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict() {

    }
}
