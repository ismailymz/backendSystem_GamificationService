package de.thws.gamification.adapter.in.rest.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DriverProfileResponseDTO extends BaseResponseDTO{
    public UUID id;
    public String username;
    public int totalPoints;
    public List<EarnedAchievementDTO> earnedAchievements;
    public List<LinkDTO> links = new ArrayList<>();
    public void addLink(String rel, String href) {
        this.links.add(new LinkDTO(rel, href));
    }
    public static class EarnedAchievementDTO {
        public UUID id;
        public String code;
        public String name;
        public String earnedAt;
    }
}