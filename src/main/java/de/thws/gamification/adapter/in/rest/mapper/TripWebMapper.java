package de.thws.gamification.adapter.in.rest.mapper;

import de.thws.gamification.adapter.in.rest.dto.request.SubmitTripRequestDTO;
import de.thws.gamification.adapter.in.rest.dto.response.SubmitTripResponseDTO;
import de.thws.gamification.domain.model.DriverAchievement;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.GamificationResult;

import java.util.UUID;
import java.util.stream.Collectors;

public class TripWebMapper {

    public static TripReport toDomain(UUID driverId, SubmitTripRequestDTO dto) {
        return TripReport.newReport(
                driverId,
                dto.distanceKm,
                dto.brakeCount,
                dto.hardAccelerationCount,
                dto.nightTrip,
                dto.startedAt,
                dto.endedAt
        );
    }

    public static SubmitTripResponseDTO toResponse(UUID driverId, GamificationResult result) {
        SubmitTripResponseDTO out = new SubmitTripResponseDTO();
        out.driverId = driverId;
        out.pointsAdded = result.getPointsAdded();
        out.totalPoints = result.getUpdatedDriver().getTotalPoints();

        out.newAchievements = result.getNewAchievements().stream()
                .map(TripWebMapper::mapAchievement)
                .collect(Collectors.toList());

        return out;
    }

    private static SubmitTripResponseDTO.NewAchievementDTO mapAchievement(DriverAchievement da) {
        SubmitTripResponseDTO.NewAchievementDTO a = new SubmitTripResponseDTO.NewAchievementDTO();
        a.id = da.getId();
        a.code = da.getAchievement().getCode();
        a.name = da.getAchievement().getName();
        a.description = da.getAchievement().getDescription();
        a.earnedAt = da.getEarnedAt().toString();
        return a;
    }
}