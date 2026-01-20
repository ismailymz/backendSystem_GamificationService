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

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripReportEntity> tripReports = new ArrayList<>();

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DriverAchievementEntity> achievements = new ArrayList<>();

    private String username;
    private int totalPoints;

    //for the authentification
    private String password;
    private String role;     // Rol (DRIVER, ADMIN vb.)

    public DriverProfileEntity() {
    }


    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public int getTotalPoints() { return totalPoints; }
    public List<TripReportEntity> getTripReports() { return tripReports; }
    public List<DriverAchievementEntity> getAchievements() { return achievements; }

    // news
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public void setId(UUID id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
    public void setAchievements(List<DriverAchievementEntity> achievements) { this.achievements = achievements; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}