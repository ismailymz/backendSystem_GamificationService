package gamification.domain;

import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverAchievement;
import de.thws.gamification.domain.model.DriverProfile;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DriverProfileTest {
   @Test
     void newDriver_shouldStartWithZeroPointsAndNoAchievements(){

         DriverProfile driver =DriverProfile.createProfile("ismail");

                 assertNotNull(driver.getId(), "id null olmamalı");
         assertEquals("ismail", driver.getUsername());
         assertEquals(0, driver.getTotalPoints(), "yeni driver 0 puanla başlamalı");
         assertTrue(driver.getEarnedAchievements().isEmpty(),
                 "başlangıçta hiç achievement olmamalı");
     }

    @Test
    void addPoints_shouldIncreaseTotalPoints() {
        DriverProfile driver = DriverProfile.createProfile("yusuf");

        driver.addPoints(30);
        assertEquals(30, driver.getTotalPoints());
    }
    @Test
    void addPoints_negative_shouldThrowException() {
        DriverProfile driver = DriverProfile.createProfile("yusuf");

        assertThrows(IllegalArgumentException.class,
                () -> driver.addPoints(-5),
                "negatif puan eklemek hata fırlatmalı");
    }
    @Test
    void addAchievement_shouldStoreAchievement() {
        DriverProfile driver = DriverProfile.createProfile("yusuf");

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
        DriverProfile driver = DriverProfile.createProfile("yusuf");

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
        DriverProfile driver = DriverProfile.createProfile("yusuf");


        driver.changeUsername("omer");


        assertEquals("omer", driver.getUsername());
    }

    @Test
    void changeUsername_blank_shouldThrowException() {
        DriverProfile driver = DriverProfile.createProfile("yusuf");

        assertThrows(IllegalArgumentException.class,
                () -> driver.changeUsername(" "),
                "boş username kabul edilmemeli");
    }


}
