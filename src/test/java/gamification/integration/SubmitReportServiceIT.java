package gamification.integration;


import de.thws.gamification.application.ports.out.AchievementRepository;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.application.service.SubmitTripReportService;
import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SubmitTripReportServiceIT {

    private static final UUID SAFE_DRIVER_ID  = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID NIGHT_DRIVER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Inject
    SubmitTripReportService submitTripService;

    @Inject DriverProfileRepository driverRepo;
    @Inject TripReportRepository tripRepo;
    @Inject AchievementRepository achievementRepo;

    private void seedSafeDriverAndNightDriverIfMissing() {

        // SAFE_DRIVER
        if (achievementRepo.findByCode("SAFE_DRIVER").isEmpty()) {
            Achievement safeDriverBadge = new Achievement(
                    SAFE_DRIVER_ID,
                    "SAFE_DRIVER",
                    "Unbreakable",
                    "Completed 5 trips without hard brakes!"
            );
            achievementRepo.save(safeDriverBadge);
        }

        // NIGHT_DRIVER
        if (achievementRepo.findByCode("NIGHT_DRIVER").isEmpty()) {
            Achievement nightOwl = new Achievement(
                    NIGHT_DRIVER_ID,
                    "NIGHT_DRIVER",
                    "Night Owl",
                    "Completed 3 trips between 22:00 and 05:00"
            );
            achievementRepo.save(nightOwl);
        }
    }

    @Test
    @TestTransaction
    void submitTrip_whenDriverExists_andTripIsNew_persistsTrip_andDoesNotCrashOnRules() {

        seedSafeDriverAndNightDriverIfMissing();

        DriverProfile driver = DriverProfile.createProfile("Erdil", "123");
        driverRepo.save(driver);


        TripReport trip = TripReport.newReport(
                driver.getId(),
                110,
                0,
                0,
                true,
                LocalDateTime.of(2026, 1, 10, 9, 10),
                LocalDateTime.of(2026, 1, 10, 9, 50)
        );

        // Act
        var result = submitTripService.submitTrip(driver.getId(), trip);


        Optional<TripReport> tripFromDb = tripRepo.findById(trip.getId());
        assertTrue(tripFromDb.isPresent(), "TripReport DB'ye persist edilmemiş");


        assertEquals(result.getPointsAdded(), tripFromDb.get().getTotalScore());
    }

    @Test
    @TestTransaction
    void submitTrip_whenTripSubmittedTwice_secondTimeReturnsZeroPoints_andDoesNotDuplicate() {

        seedSafeDriverAndNightDriverIfMissing();

        DriverProfile driver = DriverProfile.createProfile("Erdil", "123");
        driverRepo.save(driver);

        TripReport trip = TripReport.newReport(
                driver.getId(),
                110,
                1,
                1,
                true,
                LocalDateTime.of(2026, 1, 10, 9, 10),
                LocalDateTime.of(2026, 1, 10, 9, 50)
        );


        var r1 = submitTripService.submitTrip(driver.getId(), trip);
        var r2 = submitTripService.submitTrip(driver.getId(), trip);


        assertNotNull(r1);
        assertEquals(0, r2.getPointsAdded(), "Aynı Trip ikinci kez gelince 0 puan dönmeli (idempotency)");

        assertTrue(tripRepo.findById(trip.getId()).isPresent(), "Trip DB'de yok (ilk submit persist etmemiş)");
    }

    @Test
    @TestTransaction
    void submitTrip_whenDriverDoesNotExist_throwsNotFound() {

        seedSafeDriverAndNightDriverIfMissing();

        UUID missingDriverId = UUID.randomUUID();
        TripReport trip = TripReport.newReport(
                missingDriverId,
                110,
                1,
                1,
                true,
                LocalDateTime.of(2026, 1, 10, 9, 10),
                LocalDateTime.of(2026, 1, 10, 9, 50)
        );


        assertThrows(NotFoundException.class, () -> submitTripService.submitTrip(missingDriverId, trip));
    }
}
