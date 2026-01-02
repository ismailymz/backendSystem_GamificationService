package de.thws.gamification.adapter.in.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SubmitTripRequestDTO {

    @NotNull
    @Min(0)
    public Integer distanceKm;

    @NotNull
    @Min(0)
    public Integer brakeCount;

    @NotNull
    @Min(0)
    public Integer hardAccelerationCount;

    @NotNull
    public Boolean nightTrip;

    @NotNull
    public LocalDateTime startedAt;

    @NotNull
    public LocalDateTime endedAt;
}

