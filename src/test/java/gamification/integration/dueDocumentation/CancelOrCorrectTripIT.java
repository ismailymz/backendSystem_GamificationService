package gamification.integration.dueDocumentation;

import de.thws.gamification.application.ports.out.AchievementRepository;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CancelOrCorrectTripIT {

    @Inject
    DriverProfileRepository driverRepo;

    @Inject
    TripReportRepository tripRepo;

    @Inject
    AchievementRepository achievementRepo;

    @Inject
    EntityManager em;

    UUID driverId;
    UUID tripIdToVoid;
    UUID tripIdToKeep;

    @BeforeEach
    @Transactional
    void setup() {

        em.createQuery("DELETE FROM DriverAchievementEntity").executeUpdate();
        em.createQuery("DELETE FROM TripReportEntity").executeUpdate();
        em.createQuery("DELETE FROM DriverProfileEntity").executeUpdate();
        em.createQuery("DELETE FROM AchievementEntity").executeUpdate();
        Achievement safeDriverBadge = new Achievement(
                UUID.randomUUID(), "SAFE_DRIVER", "Safe Driver", "Drive safely"
        );
        achievementRepo.save(safeDriverBadge);

        DriverProfile driver = DriverProfile.createProfile("TITANIC", "pass123");
        driverRepo.save(driver);
        this.driverId = driver.getId();

        DriverProfile adminUser = DriverProfile.createAdmin("admin", "admin");
        driverRepo.save(adminUser);

        TripReport tripA = TripReport.newReport(driverId, 100, 0, 0, false, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(1));
        tripA.setTotalScore(100);
        tripRepo.save(tripA);
        this.tripIdToVoid = tripA.getId();

        TripReport tripB = TripReport.newReport(driverId, 50, 0, 0, false, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        tripB.setTotalScore(50);
        tripRepo.save(tripB);
        this.tripIdToKeep = tripB.getId();

        driver.setTotalPoints(150);
        driverRepo.save(driver);

        em.flush();
        em.clear();
    }

    @Test
    void voidTrip_asAdmin_shouldReturn200_andRecalculatePoints() {
        given()
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post("/api/admin/trips/{tripId}/void", tripIdToVoid)
                .then()
                .statusCode(200)
                .body("status", equalTo("VOIDED"))
                .body("tripId", equalTo(tripIdToVoid.toString()));


        TripReport voidedTrip = tripRepo.findById(tripIdToVoid).orElseThrow();
        assertTrue(voidedTrip.isVoided());

        DriverProfile updatedDriver = driverRepo.findById(driverId).orElseThrow();
        assertTrue(updatedDriver.getTotalPoints() < 150);
    }

    @Test
    void voidTrip_asDriver_shouldReturn403() {
        given()
                .auth().preemptive().basic("TITANIC", "pass123") // regular user
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post("/api/admin/trips/{tripId}/void", tripIdToVoid)
                .then()
                .statusCode(403); // Forbidden
    }

    @Test
    void voidTrip_unknownId_shouldReturn404() {
        given()
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post("/api/admin/trips/{uuid}/void", UUID.randomUUID())
                .then()
                .statusCode(404); // Not Found
    }
}