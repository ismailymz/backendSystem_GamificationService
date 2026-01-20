package gamification.integration;

import de.thws.gamification.application.service.RetrieveLeaderboardService;
import de.thws.gamification.application.service.SubmitTripReportService;
import de.thws.gamification.application.service.VoidTripService;
import de.thws.gamification.domain.model.Period;
import de.thws.gamification.domain.model.TripReport;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class VoidTripIntegrationTest {

    @Inject
    SubmitTripReportService submitService;

    @Inject
    VoidTripService voidService;

    @Inject
    RetrieveLeaderboardService leaderboardService;

    @Test
    void voidTrip_shouldRecomputeLeaderboardCorrectly() {

        // 1️⃣ Driver + Trip’ler
        UUID driverId = UUID.randomUUID();

        TripReport trip1 = TripReport.newReport(
                driverId,
                10,
                0,
                0,
                false,
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now().minusHours(1)
        );

        TripReport trip2 = TripReport.newReport(
                driverId,
                20,
                0,
                0,
                false,
                LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now()
        );

        //Submit
        submitService.submitTrip(driverId, trip1);
        submitService.submitTrip(driverId, trip2);

        //Leaderboard BEFORE void
        int pointsBeforeVoid =
                leaderboardService
                        .getLeaderboard(Period.ALL_TIME)
                        .get(0)
                        .getPoints();

        assertTrue(pointsBeforeVoid > 0);

        //Void second trip
        voidService.voidTrip(trip2.getId());

        //Leaderboard AFTER void
        int pointsAfterVoid =
                leaderboardService
                        .getLeaderboard(Period.ALL_TIME)
                        .get(0)
                        .getPoints();

        //Assert recompute worked
        assertTrue(
                pointsAfterVoid < pointsBeforeVoid,
                "Points should decrease after voiding a trip"
        );
    }
}
