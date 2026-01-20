package de.thws.gamification.adapter.in.rest.mapper;

import de.thws.gamification.adapter.in.rest.dto.request.SubmitTripRequestDTO;
import de.thws.gamification.adapter.in.rest.dto.response.SubmitTripResponseDTO;
import de.thws.gamification.adapter.in.rest.dto.response.TripResponseDTO; // Yeni DTO'yu import ettik
import de.thws.gamification.domain.model.DriverAchievement;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.GamificationResult;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List; // List importu eklendi
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TripWebMapper {

    // Request -> Domain
    public TripReport toDomain(UUID driverId, SubmitTripRequestDTO dto) {
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

    // Gamification Result -> Response DTO
    public SubmitTripResponseDTO toDTO(UUID driverId, GamificationResult result) {
        SubmitTripResponseDTO out = new SubmitTripResponseDTO();
        out.driverId = driverId;
        out.pointsAdded = result.getPointsAdded();

        if (result.getUpdatedDriver() != null) {
            out.totalPoints = result.getUpdatedDriver().getTotalPoints();
        }

        out.newAchievements = result.getNewAchievements().stream()
                .map(this::mapAchievement)
                .collect(Collectors.toList());

        return out;
    }

    private SubmitTripResponseDTO.NewAchievementDTO mapAchievement(DriverAchievement da) {
        SubmitTripResponseDTO.NewAchievementDTO a = new SubmitTripResponseDTO.NewAchievementDTO();
        a.id = da.getId();
        a.code = da.getAchievement().getCode();
        a.name = da.getAchievement().getName();
        a.description = da.getAchievement().getDescription();
        a.earnedAt = da.getEarnedAt().toString();
        return a;
    }


    //TripReport (Domain) -> TripResponseDTO
    public TripResponseDTO toResponse(TripReport domain) {
        TripResponseDTO dto = new TripResponseDTO();
        dto.id = domain.getId();
        dto.driverId = domain.getDriverId();
        dto.distanceKm = domain.getDistanceKm();
        dto.brakeCount = domain.getBrakeCount();
        dto.hardAccelerationCount = domain.getHardAccelerationCount();
        dto.nightTrip = domain.isNightTrip();
        dto.startedAt = domain.getStartedAt();
        dto.endedAt = domain.getEndedAt();
        dto.voided = domain.isVoided();
        return dto;
    }

    // List<TripReport> -> List<TripResponseDTO>
    public List<TripResponseDTO> toResponseList(List<TripReport> domainList) {
        if (domainList == null) {
            return List.of();
        }
        return domainList.stream()
                .map(this::toResponse) // Yukarıdaki tekil metodu kullanır
                .collect(Collectors.toList());
    }
}