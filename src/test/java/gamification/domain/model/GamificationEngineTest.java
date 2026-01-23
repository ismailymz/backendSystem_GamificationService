package gamification.domain.model;

import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverAchievement;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.AchievementRule;
import de.thws.gamification.domain.service.GamificationEngine;
import de.thws.gamification.domain.service.GamificationResult;
import de.thws.gamification.domain.service.ScoringPolicy;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GamificationEngineTest {
    @Test
    void applyTrip_shouldAddPointsAndNewAchievement() {
        UUID driverId = UUID.randomUUID();
        DriverProfile driver = DriverProfile.createProfile("kobe", "snake");

        TripReport report = TripReport.newReport(
                driverId,          // driverId
                10,                // distanceKm
                0,                 // brakeCount
                0,                 // hardAccelerationCount
                false,             // nightTrip
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(20)
        );

          ScoringPolicy scoringPolicy = (trip, drv) -> 50;

        AchievementRule rule = (trip, drv) -> {
            Achievement achievement = Achievement.of(
                    "FIRST_TRIP",
                    "First Trip",
                    "Completed first trip"
            );
            return Optional.of(achievement);
        };

        GamificationEngine engine = new GamificationEngine(
                List.of(scoringPolicy),
                List.of(rule)
        );


        GamificationResult result = engine.applyTrip(driver, report);


        assertEquals(50, result.getPointsAdded(), "Points added should be 50");

        DriverProfile updated = result.getUpdatedDriver();
        assertEquals(50, updated.getTotalPoints(), "Driver total points should be 50");

        List<DriverAchievement> newAchievements = result.getNewAchievements();
        assertEquals(1, newAchievements.size(), "Should have 1 new achievement");

        DriverAchievement earned = newAchievements.get(0);
        assertEquals("FIRST_TRIP", earned.getAchievement().getCode());
        assertNotNull(earned.getEarnedAt(), "earnedAt must not be null");


        assertEquals(1, updated.getEarnedAchievements().size());
        assertEquals("FIRST_TRIP",
                updated.getEarnedAchievements().get(0).getAchievement().getCode());
    }
}