package de.thws.gamification.config.rules;

import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.ScoringPolicy;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SafeTripBonusPolicy implements ScoringPolicy {

    @Override
    public int calculatePoints(TripReport report, DriverProfile driverProfile) {
        // güvenli sürüş bonusu (demo)
        return report.isSafeTrip() ? 50 : 0;
    }
}
