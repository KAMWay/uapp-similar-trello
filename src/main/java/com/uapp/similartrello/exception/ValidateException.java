package com.uapp.similartrello.exception;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

public class ValidateException extends RuntimeException {

    private final String errorMassages;

    public ValidateException(List<ObjectError> errors) {
        super("Validate exception");
        this.errorMassages = getStringErrors(errors);
    }

    public String getErrorMassages() {
        return errorMassages;
    }

    private String getStringErrors(List<ObjectError> globalErrors) {
        final StringBuilder sb = new StringBuilder();
        for (ObjectError error : globalErrors) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                sb.append(getErrorString(fieldError));
            } else {
                sb.append(getErrorString(error));
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    private String getErrorString(ObjectError objectError) {
        return objectError.getObjectName() + ": " +
                objectError.getDefaultMessage();
    }

    private String getErrorString(FieldError fieldError) {
        return fieldError.getField() + ": " +
                getErrorMessage(fieldError);
    }

    private String getErrorMessage(FieldError fieldError) {
        return fieldError.isBindingFailure()
                ? "invalid type"
                : fieldError.getDefaultMessage();
    }
}
