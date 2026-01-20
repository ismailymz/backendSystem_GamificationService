package de.thws.gamification.domain.service.rules;

import de.thws.gamification.application.ports.out.AchievementRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.AchievementRule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SafeDriverRule implements AchievementRule {

    private static final String BADGE_CODE = "SAFE_DRIVER";
    private static final int REQUIRED_TRIPS = 5;

    @Inject
    TripReportRepository tripRepository;

    @Inject
    AchievementRepository achievementRepository;

    @Override
    public Optional<Achievement> check(TripReport currentReport, DriverProfile driver) {

        boolean alreadyEarned = driver.getEarnedAchievements().stream()
                .anyMatch(da -> da.getAchievement().getCode().equals(BADGE_CODE));

        if (alreadyEarned) {
            return Optional.empty();
        }

        Optional<Achievement> badgeDefOpt = achievementRepository.findByCode(BADGE_CODE);


        Achievement existingBadge = badgeDefOpt.get();

        //Kural Kontrolü (Son 5 sürüş hatasız mı?)
        List<TripReport> lastTrips = tripRepository.findLastTrips(driver.getId(), REQUIRED_TRIPS);

        if (lastTrips.size() >= REQUIRED_TRIPS &&
                lastTrips.stream().allMatch(t -> t.getHardAccelerationCount() == 0 && t.getBrakeCount() == 0)) {

            return Optional.of(existingBadge);
        }

        return Optional.empty();
    }
}