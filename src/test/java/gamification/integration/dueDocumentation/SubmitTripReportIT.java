package gamification.integration.dueDocumentation;

import de.thws.gamification.adapter.in.rest.dto.request.SubmitTripRequestDTO;
import de.thws.gamification.application.ports.out.AchievementRepository;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverProfile;
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

@QuarkusTest
class SubmitTripReportIT {

    @Inject
    DriverProfileRepository driverRepo;

    @Inject
    TripReportRepository tripRepo;

    @Inject
    AchievementRepository achievementRepo;

    @Inject
    EntityManager em;

    UUID driverId;
    String username = "RyanGosling";
    String password = "driver";

    @BeforeEach
    @Transactional
    void setup() {

        em.createQuery("DELETE FROM DriverAchievementEntity").executeUpdate();
        em.createQuery("DELETE FROM TripReportEntity").executeUpdate();
        em.createQuery("DELETE FROM DriverProfileEntity").executeUpdate();
        em.createQuery("DELETE FROM AchievementEntity").executeUpdate();

        Achievement safeDriverBadge = new Achievement(
                UUID.randomUUID(),
                "SAFE_DRIVER",
                "Safe Driver",
                "Drive safely"
        );
        achievementRepo.save(safeDriverBadge);

        DriverProfile driver = DriverProfile.createProfile(username, password);
        driverRepo.save(driver);

        em.flush();
        em.clear();

        this.driverId = driver.getId();
    }

    @Test
    void submitTrip_validData_shouldReturn201_andPoints() {

        SubmitTripRequestDTO request = new SubmitTripRequestDTO();
        request.distanceKm = 100;
        request.brakeCount = 0;
        request.hardAccelerationCount = 0;
        request.nightTrip = false;
        request.startedAt = LocalDateTime.now().minusHours(2);
        request.endedAt = LocalDateTime.now().minusHours(1);


        given()
                .auth().preemptive().basic(username, password)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/trips/driver/{driverId}", driverId)
                .then()
                .statusCode(201) // Created
                .body("driverId", equalTo(driverId.toString()))
                .body("pointsAdded", greaterThan(0)) // points check
                .body("totalPoints", greaterThan(0)); // updated?

        // database control
        given()
                .auth().preemptive().basic(username, password)
                .when()
                .get("/api/trips/driver/{driverId}", driverId)
                .then()
                .statusCode(200)
                .body("trips", hasSize(1));
    }

    @Test
    void submitTrip_invalidData_shouldReturn400() {

        SubmitTripRequestDTO request = new SubmitTripRequestDTO();
        request.distanceKm = -50;
        request.brakeCount = 0;
        request.hardAccelerationCount = 0;
        request.nightTrip = false;
        request.startedAt = LocalDateTime.now();
        request.endedAt = LocalDateTime.now().plusHours(1);

        given()
                .auth().preemptive().basic(username, password)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/trips/driver/{driverId}", driverId)
                .then()
                .statusCode(400); // Bad Request
    }

    @Test
    void submitTrip_unauthorized_shouldReturn401_or_403() {

        SubmitTripRequestDTO request = new SubmitTripRequestDTO();
        request.distanceKm = 50;
        request.brakeCount = 0;
        request.hardAccelerationCount = 0;
        request.nightTrip = false;
        request.startedAt = LocalDateTime.now();
        request.endedAt = LocalDateTime.now().plusHours(1);

        given()
                .auth().preemptive().basic(username, "WRONG_PASSWORD")
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/trips/driver/{driverId}", driverId)
                .then()
                .statusCode(anyOf(is(401), is(403)));
    }
}