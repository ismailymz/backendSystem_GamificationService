package de.thws.gamification.adapter.out.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "driver_profiles")
public class DriverProfileEntity {



    @Id
    private UUID id;

    @OneToMany(mappedBy = "driver")
    private List<TripReportEntity> tripReports = new ArrayList<>();

    @OneToMany(mappedBy = "driver")
    private List<DriverAchievementEntity> achievements = new ArrayList<>();

    private String username;
    private int totalPoints;

    public DriverProfileEntity() {

    }

    //getter
    public UUID getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public int getTotalPoints() {
        return totalPoints;
    }
    //setter
    public void setId(UUID id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
    public List<TripReportEntity> getTripReports() {
        return tripReports;
    }
    public List<DriverAchievementEntity> getAchievements() {
        return achievements;
    }


}