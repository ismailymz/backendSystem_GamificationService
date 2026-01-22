package gamification.domain.service;

import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import org.junit.jupiter.api.Test;
import de.thws.gamification.domain.service.rules.*;

import java.time.LocalDateTime;

import static de.thws.gamification.domain.model.DriverProfile.createProfile;
import static de.thws.gamification.domain.model.TripReport.newReport;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RulesTest {


    DriverProfile driver = createProfile("Erdil", "erdil123");
    TripReport report = newReport(driver.getId(),
            120,
            3,
            4,
            true,
            LocalDateTime.of(2026, 1, 14, 16, 45),
            LocalDateTime.of(2026, 1, 14, 18, 52) );

    @Test
    void calculatePointsWithReportAndDriver(){
        DistanceScoringPolicy policy = new DistanceScoringPolicy();
        assertEquals(120, policy.calculatePoints(report, driver));
    }





}
