package gamification.domain.model;

import de.thws.gamification.domain.model.TripReport;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TripReportTest {
    @Test
    void newReport_shouldCreateValidTripWithRandomId() {
        UUID driverId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(30);


        TripReport report = TripReport.newReport(
                driverId,
                50,
                1,
                2,
                false,
                start,
                end
        );


        assertNotNull(report.getId(), "Id cannot be null");
        assertEquals(driverId, report.getDriverId());
        assertEquals(50, report.getDistanceKm());
        assertFalse(report.isVoided(), "New created report should not be voided");
    }

    @Test
    void getDuration_shouldReturnCorrectDurationInMinutes() {
        UUID driverId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(45);

        TripReport report = TripReport.newReport(
                driverId,
                30,
                0,
                0,
                false,
                start,
                end
        );


        Duration duration = report.getDuration();


        assertEquals(45, duration.toMinutes(), "Duration should be 45 minutes");
    }

    @Test
    void isSafeTrip_shouldBeTrueWhenNoBrakesAndNoHardAcceleration() {
        TripReport safeTrip = TripReport.newReport(
                UUID.randomUUID(),
                20,
                0,
                0,
                false,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10)
        );

        assertTrue(safeTrip.isSafeTrip(), "If no brakes and no hard acceleration, should be safe trip");

        TripReport unsafeTrip = TripReport.newReport(
                UUID.randomUUID(),
                20,
                1,
                0,
                false,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10)
        );

        assertFalse(unsafeTrip.isSafeTrip(), "With hard brakes, should not be safe trip");
    }

    @Test
    void markVoided_shouldSetVoidedFlagTrue() {
        TripReport report = TripReport.newReport(
                UUID.randomUUID(),
                10,
                0,
                0,
                false,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5)
        );




        report.markVoided();


        assertTrue(report.isVoided(), "After marking voided, isVoided should be true");
    }
}