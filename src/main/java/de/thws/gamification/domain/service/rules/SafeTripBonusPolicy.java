package de.thws.gamification.domain.service.rules;

import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.ScoringPolicy;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SafeTripBonusPolicy implements ScoringPolicy {

    @Override
    public int calculatePoints(TripReport report, DriverProfile driver) {
        // Eğer ani fren ve ani hızlanma yoksa +5 Bonus Puan
        if (report.getBrakeCount() == 0 && report.getHardAccelerationCount() == 0) {
            return 5;
        }
        return 0;
    }
}