package gamification.domain.model;

import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverAchievement;
import de.thws.gamification.domain.model.DriverProfile;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DriverProfileTest {
    @Test
    void newDriver_shouldStartWithZeroPointsAndNoAchievements(){

        DriverProfile driver =DriverProfile.createProfile("ismail", "BACKEND");

        assertNotNull(driver.getId(), "Id can't be null");
        assertEquals("ismail", driver.getUsername());
        assertEquals(0, driver.getTotalPoints(), "New driver must be created with 0 points");
        assertTrue(driver.getEarnedAchievements().isEmpty(),
                "At the beginning, driver should have no achievements");
    }

    @Test
    void addPoints_shouldIncreaseTotalPoints() {
        DriverProfile driver = DriverProfile.createProfile("Alexandir", "fight club?");

        driver.addPoints(30);
        assertEquals(30, driver.getTotalPoints());
    }
    @Test
    void addPoints_negative_shouldThrowException() {
        DriverProfile driver = DriverProfile.createProfile("Turing", "enigma");

        assertThrows(IllegalArgumentException.class,
                () -> driver.addPoints(-5),
                "Negative point addition should throw exception");
    }
    @Test
    void addAchievement_shouldStoreAchievement() {
        DriverProfile driver = DriverProfile.createProfile("Johnny Bravo", "superman");

        Achievement achievement = Achievement.of(
                "SAFE_TRIP",
                "Safe Trip",
                "No hard brakes and accelerations"
        );

        DriverAchievement earned = DriverAchievement.of(
                driver.getId(),
                achievement,
                LocalDateTime.now()
        );

        driver.addAchievement(earned);

        assertEquals(1, driver.getEarnedAchievements().size());
        assertEquals("SAFE_TRIP",
                driver.getEarnedAchievements().get(0).getAchievement().getCode());
    }

    @Test
    void getEarnedAchievements_shouldBeUnmodifiable() {
        DriverProfile driver = DriverProfile.createProfile("inception", "dreams");

        Achievement achievement = Achievement.of(
                "SAFE_TRIP",
                "Safe Trip",
                "No hard brakes and accelerations"
        );
        DriverAchievement earned = DriverAchievement.of(
                driver.getId(),
                achievement,
                LocalDateTime.now()
        );
        driver.addAchievement(earned);

        assertThrows(UnsupportedOperationException.class, () -> {
            driver.getEarnedAchievements().add(earned);
        });
    }
    @Test
    void changeUsername_shouldUpdateUsername() {
        DriverProfile driver = DriverProfile.createProfile("alibaba", "open sesame");


        driver.changeUsername("omer");


        assertEquals("omer", driver.getUsername());
    }

    @Test
    void changeUsername_blank_shouldThrowException() {
        DriverProfile driver = DriverProfile.createProfile("techguy", "linuxrules");

        assertThrows(IllegalArgumentException.class,
                () -> driver.changeUsername(" "),
                "username cannot be blank");
    }


}