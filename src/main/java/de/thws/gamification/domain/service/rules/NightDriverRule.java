package de.thws.gamification.domain.service.rules;

import de.thws.gamification.application.ports.out.AchievementRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.AchievementRule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NightDriverRule implements AchievementRule {

    private static final String BADGE_CODE = "NIGHT_DRIVER";
    private static final int REQUIRED_NIGHT_TRIPS = 3;
    private static final int LOOKBACK_LIMIT = 10; // last 10

    @Inject
    TripReportRepository tripRepository;

    @Inject
    AchievementRepository achievementRepository;

    @Override
    public Optional<Achievement> check(TripReport currentReport, DriverProfile driver) {


        boolean alreadyEarned = driver.getEarnedAchievements().stream()
                .anyMatch(da -> da.getAchievement().getCode().equals(BADGE_CODE));
        if (alreadyEarned) return Optional.empty();

        Optional<Achievement> badgeDef = achievementRepository.findByCode(BADGE_CODE);
        if (badgeDef.isEmpty()) return Optional.empty();

        List<TripReport> dbTrips = tripRepository.findLastTrips(driver.getId(), LOOKBACK_LIMIT);

        List<TripReport> tripsToCheck = new ArrayList<>(dbTrips);
        boolean containsCurrent = tripsToCheck.stream().anyMatch(t -> t.getId().equals(currentReport.getId()));

        if (!containsCurrent) {
            tripsToCheck.add(currentReport);
        }

        long nightTripCount = tripsToCheck.stream()
                .filter(this::isNightTrip)
                .count();


        if (nightTripCount >= REQUIRED_NIGHT_TRIPS) {
            return Optional.of(badgeDef.get());
        }

        return Optional.empty();
    }

    private boolean isNightTrip(TripReport trip) {
        LocalDateTime start = trip.getStartedAt();
        int hour = start.getHour();
        // 22, 23, 00, 01, 02, 03, 04
        return hour >= 22 || hour < 5;
    }
}