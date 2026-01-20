package de.thws.gamification.domain.service.rules;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.ScoringPolicy;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DistanceScoringPolicy implements ScoringPolicy {

    @Override
    public int calculatePoints(TripReport report, DriverProfile driver) {
        // Basit Kural Her 1 KM = 1 Point
        int points = (int) report.getDistanceKm();
        return points;
    }
}