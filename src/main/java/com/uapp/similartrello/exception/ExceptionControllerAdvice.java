package com.uapp.similartrello.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import java.sql.SQLException;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        final Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        final HttpStatus status;
        final String message;

        if (isNull(exception)) {
            status = NOT_FOUND;
            message = "Page not found";
        } else {
            status = INTERNAL_SERVER_ERROR;
            message = status.getReasonPhrase();
        }

        return new ModelAndView("error")
                .addObject("message", message)
                .addObject("status", status);
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public String employeeNotFoundHandler(NotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(BAD_REQUEST)
    protected String employeeSQLExceptionHandler() {
        return "Exception work sql query";
    }

    @ResponseBody
    @ExceptionHandler(ValidateException.class)
    @ResponseStatus(BAD_REQUEST)
    protected String onValidateException(ValidateException ex) {
        return ex.getErrorMassages().replaceAll(System.lineSeparator(), "<br>");
    }

}
