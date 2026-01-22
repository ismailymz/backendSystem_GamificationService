package de.thws.gamification.adapter.out.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trip_reports")
public class TripReportEntity {


    @Id
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "driver_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_trip_report_driver_id")
    )
    private DriverProfileEntity driver;


    private int distanceKm;
    private int brakeCount;
    private int hardAccelerationCount;
    private boolean nightTrip;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;


    @Column(nullable = false)
    private boolean voided;
    //i had to add this for points and leaderboard but it still shoul be fixed
    @Column(name = "points", nullable = false, columnDefinition = "integer default 0")
    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public TripReportEntity() {

    }
    //getter ----------------
    public UUID getId() {
        return id;
    }
    public DriverProfileEntity getDriver() {
        return driver;
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

    //setter ---------------
    public void setId(UUID id) {
        this.id = id;
    }
    public void setDriver(DriverProfileEntity driver) {
        this.driver = driver;
    }
    public void setDistanceKm(int distanceKm) {
        this.distanceKm = distanceKm;
    }
    public void setBrakeCount(int brakeCount) {
        this.brakeCount = brakeCount;
    }
    public void setHardAccelerationCount(int hardAccelerationCount) {
        this.hardAccelerationCount = hardAccelerationCount;
    }
    public void setNightTrip(boolean nightTrip) {
        this.nightTrip = nightTrip;
    }
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }
    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }
    public void setVoided(boolean voided) {
        this.voided = voided;
    }


}