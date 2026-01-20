package de.thws.gamification.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DriverProfile {
    private final UUID id;
    private String username;
    private String password; // Authentication (Secret)
    private String role;     // Authorization (User Group)

    private int totalPoints;
    private final List<DriverAchievement> earnedAchievements = new ArrayList<>();

    public DriverProfile(String username, String password, String role, int totalPoints) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be blank");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role must not be blank");
        }
        if (totalPoints < 0) {
            throw new IllegalArgumentException("Points can not be negative");
        }

        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.role = role;
        this.totalPoints = totalPoints;
    }

    public DriverProfile(UUID id, String username, String password, String role, int totalPoints) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        if (totalPoints < 0) {
            throw new IllegalArgumentException("Points can not be negative");
        }

        this.id = id;
        this.username = username;
        this.password = password; // DB'den gelen şifre
        this.role = role;         // DB'den gelen rol
        this.totalPoints = totalPoints;
    }

    public static DriverProfile createProfile(String username, String password) {
        return new DriverProfile(username, password, "DRIVER", 0);
    }

    public static DriverProfile createAdmin(String username, String password) {
        return new DriverProfile(username, password, "ADMIN", 0);
    }

    public void addPoints(int pointsAdd) {
        if (pointsAdd < 0) {
            throw new IllegalArgumentException("pointsToAdd must not be negative");
        }
        this.totalPoints += pointsAdd;
    }

    public void resetPoints() {
        this.totalPoints = 0;
    }

    public void addAchievement(DriverAchievement achievement) {
        if (achievement == null) {
            throw new IllegalArgumentException("achievement can not be null");
        }
        this.earnedAchievements.add(achievement);
    }

    public void clearAchievements() {
        this.earnedAchievements.clear();
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setTotalPoints(int totalPoints) {
        if (totalPoints < 0) throw new IllegalArgumentException("Points cannot be negative");
        this.totalPoints = totalPoints;
    }


    public int getTotalPoints() {
        return totalPoints;
    }

    public List<DriverAchievement> getEarnedAchievements() {
        return Collections.unmodifiableList(earnedAchievements);
    }

    public void changeUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username must not be blank");
        }
        this.username = username;
    }

    @Override
    public String toString() {
        return "DriverProfile{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' + // Rolü de loglarda görelim
                ", totalPoints=" + totalPoints +
                ", earnedAchievements=" + earnedAchievements.size() +
                '}';
    }
}