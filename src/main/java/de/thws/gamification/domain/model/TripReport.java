package de.thws.gamification.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class TripReport {
    private final UUID id;
    private final UUID driverId;

    private final int distanceKm;
    private final int brakeCount;
    private final int hardAccelerationCount;
    private final boolean nightTrip;

    private final LocalDateTime startedAt;
    private final LocalDateTime endedAt;
    private boolean voided;

    public TripReport(UUID id,
                      UUID driverId,
                      int distanceKm,
                      int brakeCount,
                      int hardAccelerationCount,
                      boolean nightTrip,
                      LocalDateTime startedAt,
                      LocalDateTime endedAt) {

        if (id == null) throw new IllegalArgumentException("id must not be null");
        if (driverId == null) throw new IllegalArgumentException("driverId must not be null");
        if (distanceKm < 0) throw new IllegalArgumentException("distanceKm must not be negative");
        if (brakeCount < 0) throw new IllegalArgumentException("brakeCount must not be negative");
        if (hardAccelerationCount < 0) throw new IllegalArgumentException("hardAccelerationCount must not be negative");
        if (startedAt == null || endedAt == null) throw new IllegalArgumentException("start/end times cannot be null");
        if (endedAt.isBefore(startedAt)) throw new IllegalArgumentException("Trip cannot end before it starts");

        this.voided=false;

        this.id = id;
        this.driverId = driverId;
        this.distanceKm = distanceKm;
        this.brakeCount = brakeCount;
        this.hardAccelerationCount = hardAccelerationCount;
        this.nightTrip = nightTrip;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }
    public static TripReport newReport(UUID driverId, int distanceKm, int brakeCount, int hardAccelerationCount, boolean nightTrip, LocalDateTime startedAt, LocalDateTime endedAt) {
        return new TripReport(UUID.randomUUID(), driverId, distanceKm, brakeCount, hardAccelerationCount, nightTrip, startedAt, endedAt
        );
}
//bu süre hesaplamak için
    public Duration getDuration() {
        return Duration.between(startedAt, endedAt);
    }
//buda sürüş güvenli mi anlamak için
    public boolean isSafeTrip() {
        return brakeCount == 0 && hardAccelerationCount == 0;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public int getDistanceKm() {
        return distanceKm;
    }

    public int getBrakeCount() {
        return brakeCount;
    }

    public int getHardAccelerationCount() {
        return hardAccelerationCount;
    }

    public boolean isNightTrip() {
        return nightTrip;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }
    public boolean isVoided() {
        return voided;
    }

    public void markVoided() {
        this.voided = true;
    }

    @Override
    public String toString() {
        return "TripReport{" +
                "id=" + id +
                ", driverId=" + driverId +
                ", distanceKm=" + distanceKm +
                ", brakeCount=" + brakeCount +
                ", hardAccelerationCount=" + hardAccelerationCount +
                ", nightTrip=" + nightTrip +
                ", start=" + startedAt +
                ", end=" + endedAt +
                '}';
    }
}
