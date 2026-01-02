package de.thws.gamification.adapter.in.rest.dto.response;
import java.util.List;

public class ErrorResponseDTO {
    public String message;
    public String errorCode; // opsiyonel
    public List<FieldErrorDTO> errors; // opsiyonel
}