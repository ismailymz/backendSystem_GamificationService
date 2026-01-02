package de.thws.gamification.adapter.in.rest.dto.response;


import java.util.UUID;

public class LeaderboardEntryDTO {
    public UUID driverId;
    public String username;
    public int points;
    public int rank;
}