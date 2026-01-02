package de.thws.gamification.domain.model;

import java.util.UUID;

public class LeaderboardEntry {
    private final UUID driverId;
    private final String username;
    private final int totalPoints;
    private final int rank;

    public LeaderboardEntry(UUID driverId, String username, int totalPoints, int rank) {
        if (driverId == null) throw new IllegalArgumentException("driverId must not be null");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("username must not be blank");
        if (totalPoints < 0) throw new IllegalArgumentException("points cannot be negative");
        if (rank <= 0) throw new IllegalArgumentException("rank must be positive");

        this.driverId = driverId;
        this.username = username;
        this.totalPoints = totalPoints;
        this.rank = rank;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public String getUsername() {
        return username;
    }

    public int getPoints() {
        return totalPoints;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return "LeaderboardEntry{" +
                "driverId=" + driverId +
                ", username='" + username + '\'' +
                ", totalPoints=" + totalPoints +
                ", rank=" + rank +
                '}';
    }
}
