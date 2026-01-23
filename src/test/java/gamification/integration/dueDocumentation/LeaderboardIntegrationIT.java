package gamification.integration.dueDocumentation;

import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.GamificationEngine;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class LeaderboardIntegrationIT {

    @Inject
    GamificationEngine engine;

    @Inject
    DriverProfileRepository driverRepo;

    @Inject
    TripReportRepository tripRepo;

    @Test
    void leaderboard_shouldReturnDriversInCorrectOrder() {


        DriverProfile d1 = DriverProfile.createProfile("Steph", "chef");
        DriverProfile d2 = DriverProfile.createProfile("James", "king");
        DriverProfile d3 = DriverProfile.createProfile("Alperen", "sengun");


        TripReport tripSteph = TripReport.newReport(
                d1.getId(), 50, 0, 0, false,
                LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1)
        );
        tripSteph.setTotalScore(50);


        d1.setTotalPoints(50);


        driverRepo.save(d1);
        tripRepo.save(tripSteph);

        TripReport tripJames = TripReport.newReport(
                d2.getId(), 200, 2, 1, true,
                LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(10).plusHours(2)
        );
        tripJames.setTotalScore(200);

        d2.setTotalPoints(200);


        driverRepo.save(d2);
        tripRepo.save(tripJames);

        TripReport tripAlperen = TripReport.newReport(
                d3.getId(), 150, 0, 0, false,
                LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3).plusHours(1)
        );
        tripAlperen.setTotalScore(150);
        d3.setTotalPoints(150);

        driverRepo.save(d3);
        tripRepo.save(tripAlperen);

        //TEST 1 DAILY
        given()
                .queryParam("period", "DAILY")
                .when()
                .get("/api/leaderboard")
                .then()
                .statusCode(200)
                .body("entries", hasSize(1)) // Sadece 1 kişi olmalı
                .body("entries[0].username", is("Steph"));

        //TEST 2 WEEKLY
        given()
                .queryParam("period", "WEEKLY")
                .when()
                .get("/api/leaderboard")
                .then()
                .statusCode(200)
                .body("entries", hasSize(2)) // James not exist int hos entries so 2
                .body("entries[0].username", is("Alperen")) // 150 points first place
                .body("entries[1].username", is("Steph")) // 50 points second place
                .body("entries.username", not(hasItem("James")));

        //TEST 3 ALLTIME
        given()
                .queryParam("period", "ALL_TIME")
                .when()
                .get("/api/leaderboard")
                .then()
                .statusCode(200)
                .body("entries", hasSize(3))
                .body("entries[0].username", is("James")) // 200 Points
                .body("entries[1].username", is("Alperen")) // 150 Points
                .body("entries[2].username", is("Steph"));// 50 Points
}
}