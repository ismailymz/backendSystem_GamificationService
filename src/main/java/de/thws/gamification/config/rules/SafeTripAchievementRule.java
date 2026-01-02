package de.thws.gamification.config.rules;

import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.AchievementRule;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SafeTripAchievementRule implements AchievementRule {

    // demo achievement (port/out repo’ya ihtiyaç yok)
    private static final Achievement SAFE_TRIP =
            Achievement.of("SAFE_TRIP", "Safe Trip", "Completed a trip with no hard brake/acceleration.");

    @Override
    public Optional<Achievement> check(TripReport report, DriverProfile driver) {
        if (!report.isSafeTrip()) return Optional.empty();

        boolean alreadyHas = driver.getEarnedAchievements().stream()
                .anyMatch(da -> "SAFE_TRIP".equals(da.getAchievement().getCode()));

        return alreadyHas ? Optional.empty() : Optional.of(SAFE_TRIP);
    }
}