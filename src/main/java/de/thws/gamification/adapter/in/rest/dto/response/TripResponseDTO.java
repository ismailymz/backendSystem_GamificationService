package de.thws.gamification.adapter.in.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class TripResponseDTO {
    public UUID id;
    public UUID driverId;
    public int distanceKm;
    public int brakeCount;
    public int hardAccelerationCount;
    public boolean nightTrip;
    public LocalDateTime startedAt;
    public LocalDateTime endedAt;
    public boolean voided;
}
