package de.thws.gamification.adapter.out.persistence.mapper;

import de.thws.gamification.adapter.out.persistence.entity.DriverProfileEntity;
import de.thws.gamification.adapter.out.persistence.entity.TripReportEntity;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
@ApplicationScoped
public class TripReportMapper {
    @Inject
    DriverProfileMapper driverProfileMapper;

    //To Entity

    public TripReportEntity toEntity(TripReport report, DriverProfileEntity driverRef) {
        TripReportEntity e = new TripReportEntity();
        e.setId(report.getId());
        e.setDriver(driverRef);
        e.setDistanceKm(report.getDistanceKm());
        e.setBrakeCount(report.getBrakeCount());
        e.setHardAccelerationCount(report.getHardAccelerationCount());
        e.setNightTrip(report.isNightTrip());
        e.setStartedAt(report.getStartedAt());
        e.setPoints(report.getTotalScore());
        e.setEndedAt(report.getEndedAt());
        e.setVoided(report.isVoided());
        return e;
    }

    public List<TripReportEntity> toEntity(List<TripReport> tripReports, DriverProfileEntity driverRef){
        return tripReports.stream().map(tr -> toEntity(tr, driverRef)).toList();
    }

    //To Domain

    public TripReport toDomain(TripReportEntity tripReportEntity) {

        TripReport report = new TripReport(
                tripReportEntity.getId(),
                tripReportEntity.getDriver().getId(),
                tripReportEntity.getDistanceKm(),
                tripReportEntity.getBrakeCount(),
                tripReportEntity.getHardAccelerationCount(),
                tripReportEntity.isNightTrip(),
                tripReportEntity.getStartedAt(),
                tripReportEntity.getEndedAt()
        );

        // Veritabanındaki puanı Domain nesnesine aktarıyoruz.
        // Bu olmazsa, veritabanında puan olsa bile kod içinde göremeyiz.
        report.setTotalScore(tripReportEntity.getPoints());

        if (tripReportEntity.isVoided()) {
            report.markVoided();
        }

        return report;
    }
    public List<TripReport> toDomains(List<TripReportEntity> tripReportEntities){
        return tripReportEntities.stream().map(this::toDomain).toList();
    }

}
