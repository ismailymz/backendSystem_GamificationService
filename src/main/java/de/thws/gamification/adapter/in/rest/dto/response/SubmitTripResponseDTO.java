package de.thws.gamification.adapter.in.rest.dto.response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SubmitTripResponseDTO {
    public UUID driverId;
    public int pointsAdded;
    public int totalPoints;

    public List<NewAchievementDTO> newAchievements;
    //HATEOAS
    //Links
    public List<LinkDTO> links = new ArrayList<>();

    public void addLink(String rel, String href) {
        this.links.add(new LinkDTO(rel, href));
    }

    public static class NewAchievementDTO {
        public UUID id;
        public String code;
        public String name;
        public String description;
        public String earnedAt; // ISO string yazmak kolay (LocalDateTime da olabilir)
    }}