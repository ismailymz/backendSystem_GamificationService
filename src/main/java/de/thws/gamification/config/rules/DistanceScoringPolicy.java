package de.thws.gamification.config.rules;


import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.ScoringPolicy;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DistanceScoringPolicy implements ScoringPolicy {

    @Override
    public int calculatePoints(TripReport report, DriverProfile driverProfile) {
        // 1 km = 1 point (demo)
        return report.getDistanceKm();
    }
}