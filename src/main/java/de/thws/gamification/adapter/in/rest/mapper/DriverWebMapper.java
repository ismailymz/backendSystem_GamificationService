package de.thws.gamification.adapter.in.rest.mapper;

import de.thws.gamification.adapter.in.rest.dto.response.DriverProfileResponseDTO;
import de.thws.gamification.domain.model.DriverAchievement;
import de.thws.gamification.domain.model.DriverProfile;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.stream.Collectors;

@ApplicationScoped
public class DriverWebMapper {

    public DriverProfileResponseDTO toDTO(DriverProfile driver) {
        DriverProfileResponseDTO out = new DriverProfileResponseDTO();
        out.id = driver.getId();
        out.username = driver.getUsername();
        out.totalPoints = driver.getTotalPoints();
        out.earnedAchievements = driver.getEarnedAchievements().stream()
                .map(this::mapEarned)
                .collect(Collectors.toList());

        return out;
    }

    private DriverProfileResponseDTO.EarnedAchievementDTO mapEarned(DriverAchievement da) {
        DriverProfileResponseDTO.EarnedAchievementDTO a = new DriverProfileResponseDTO.EarnedAchievementDTO();
        a.id = da.getId();
        a.code = da.getAchievement().getCode();
        a.name = da.getAchievement().getName();
        a.earnedAt = da.getEarnedAt().toString();
        return a;
    }
}