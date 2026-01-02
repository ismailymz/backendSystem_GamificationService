package de.thws.gamification.adapter.in.rest.dto.response;

import java.util.List;
import java.util.UUID;

public class DriverProfileResponseDTO {
    public UUID id;
    public String username;
    public int totalPoints;
    public List<EarnedAchievementDTO> earnedAchievements;

    public static class EarnedAchievementDTO {
        public UUID id;
        public String code;
        public String name;
        public String earnedAt;
    }
}