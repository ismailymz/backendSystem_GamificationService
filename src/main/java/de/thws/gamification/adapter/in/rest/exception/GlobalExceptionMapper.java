package de.thws.gamification.adapter.in.rest.exception;

import de.thws.gamification.adapter.in.rest.dto.response.ErrorResponseDTO;
import de.thws.gamification.adapter.in.rest.dto.response.FieldErrorDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {

        // 1) Validation
        if (ex instanceof ConstraintViolationException cve) {
            return build(Response.Status.BAD_REQUEST, "VALIDATION_ERROR", "Validation failed",
                    cve.getConstraintViolations());
        }

        // 2) IllegalArgument -> 400
        if (ex instanceof IllegalArgumentException iae) {
            return simple(Response.Status.BAD_REQUEST, "BAD_REQUEST", iae.getMessage());
        }

        // 3) Not found -> 404
        if (ex instanceof NoSuchElementException nse) {
            return simple(Response.Status.NOT_FOUND, "NOT_FOUND", nse.getMessage());
        }

        // 4) JAX-RS exceptions (BadRequestException vs.)
        if (ex instanceof WebApplicationException wae) {
            int status = wae.getResponse().getStatus();
            String code = "HTTP_" + status;
            String msg = wae.getMessage();
            return simple(Response.Status.fromStatusCode(status), code, msg);
        }

        // 5) Fallback -> 500
        return simple(Response.Status.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", ex.getMessage());
    }

    private Response simple(Response.Status status, String code, String message) {
        ErrorResponseDTO dto = new ErrorResponseDTO();
        dto.errorCode = code;
        dto.message = (message == null || message.isBlank()) ? status.getReasonPhrase() : message;
        return Response.status(status).entity(dto).build();
    }

    private Response build(Response.Status status, String code, String message,
                           Iterable<ConstraintViolation<?>> violations) {

        ErrorResponseDTO dto = new ErrorResponseDTO();
        dto.errorCode = code;
        dto.message = message;

        List<FieldErrorDTO> errors = ((List<ConstraintViolation<?>>) toList(violations)).stream()
                .map(v -> {
                    FieldErrorDTO fe = new FieldErrorDTO();
                    fe.field = v.getPropertyPath() == null ? null : v.getPropertyPath().toString();
                    fe.message = v.getMessage();
                    return fe;
                })
                .collect(Collectors.toList());

        dto.errors = errors;
        return Response.status(status).entity(dto).build();
    }

    private List<ConstraintViolation<?>> toList(Iterable<ConstraintViolation<?>> it) {
        return (it instanceof List<?> l) ? (List<ConstraintViolation<?>>) l :
                ((java.util.Set<ConstraintViolation<?>>) it).stream().collect(Collectors.toList());
    }
}