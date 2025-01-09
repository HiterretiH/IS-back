package org.lab.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.core.Response;
import org.lab.model.User;

import java.util.Set;

public class ModelValidator {
    private static Set<ConstraintViolation<?>> lastViolations = null;
    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static boolean validate(User entity) {
        Set<ConstraintViolation<User>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            lastViolations = (Set<ConstraintViolation<?>>) (Set<?>) violations;
            return false;
        }

        lastViolations = null;
        return true;
    }

    public static <T> boolean validate(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            lastViolations = (Set<ConstraintViolation<?>>) (Set<?>) violations;
            return false;
        }

        lastViolations = null;
        return true;
    }

    public static String getLastValidationErrorMessage() {
        if (lastViolations == null || lastViolations.isEmpty()) {
            return null;
        }

        StringBuilder errorMessages = new StringBuilder();
        for (ConstraintViolation<?> violation : lastViolations) {
            errorMessages.append(violation.getPropertyPath())
                    .append(" ")
                    .append(violation.getMessage())
                    .append("; ");
        }
        return errorMessages.toString();
    }

    public static Response getValidationErrorResponse() {
        String errorMessage = getLastValidationErrorMessage();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Validation failed: " + errorMessage)
                .build();
    }
}
