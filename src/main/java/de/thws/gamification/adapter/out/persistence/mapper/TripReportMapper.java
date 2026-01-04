package de.thws.gamification.adapter.out.persistence.mapper;

import de.thws.gamification.adapter.out.persistence.entity.TripReportEntity;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import jakarta.inject.Inject;

import java.util.List;

public class TripReportMapper {
    @Inject
    DriverProfileMapper driverProfileMapper;

    //To Entity

    public TripReportEntity toEntity(TripReport tripReport, DriverProfile driverProfile){
        final var returnResult = new TripReportEntity();
        returnResult.setId(tripReport.getId());
        returnResult.setDriver(driverProfileMapper.toEntity(driverProfile));
        returnResult.setDistanceKm(tripReport.getDistanceKm());
        returnResult.setBrakeCount(tripReport.getBrakeCount());
        returnResult.setHardAccelerationCount(tripReport.getHardAccelerationCount());
        returnResult.setNightTrip(tripReport.isNightTrip());
        returnResult.setStartedAt(tripReport.getStartedAt());
        returnResult.setEndedAt(tripReport.getEndedAt());
        return returnResult;
    }

    public List<TripReportEntity> toEntity(List<TripReport> tripReports, DriverProfile driverProfile){
        return tripReports.stream().map(tr -> toEntity(tr, driverProfile)).toList();
    }

    //To Domain

    public TripReport toDomain(TripReportEntity tripReportEntity){
        return new TripReport(
                tripReportEntity.getId(),
                tripReportEntity.getDriver().getId(),
                tripReportEntity.getDistanceKm(),
                tripReportEntity.getBrakeCount(),
                tripReportEntity.getHardAccelerationCount(),
                tripReportEntity.isNightTrip(),
                tripReportEntity.getStartedAt(),
                tripReportEntity.getEndedAt()
        );
    }

    public List<TripReport> toDomains(List<TripReportEntity> tripReportEntities){
        return tripReportEntities.stream().map(this::toDomain).toList();
    }

}
