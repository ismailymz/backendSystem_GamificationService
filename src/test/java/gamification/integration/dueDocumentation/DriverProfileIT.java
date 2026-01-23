package gamification.integration.dueDocumentation;

import de.thws.gamification.application.ports.in.SubmitTripReportUseCase;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

//view profile as the same user, create profile, view trips as the same user, update profile
@QuarkusTest

public class DriverProfileIT {
//public Response createDriver(@Valid CreateDriverRequestDTO request)
        @Inject
        DriverProfileRepository driverRepo;
        @Inject
        TripReportRepository reportRepo;
        @Inject
        EntityManager em;
    @AfterEach
    @Transactional
    void cleanup() {
        em.createNativeQuery("DELETE FROM trip_report").executeUpdate();
        em.createNativeQuery("DELETE FROM driver_profile").executeUpdate();

    }
    @Test
    void viewProfile_asSameUser_shouldReturn200() {

        DriverProfile driver = DriverProfile.createProfile("Cedric", "iLoveChen");
        driverRepo.save(driver);

        String driverId = driver.getId().toString();

        given()
                .auth().preemptive().basic("Cedric", "iLoveChen")
                .when()
                .get("/api/drivers/{id}/profile", driverId)
                .then()
                .statusCode(200)
                .body("id", equalTo(driverId))
                .body("username", equalTo("Cedric"));
    }

    @Test
        @TestTransaction

//This test creates a driver profile via POST request and checks if it is persisted(correctly) in the database.
    void create_Profile_WithRequest_AndCheck_IfStatusCode201_And_IfPersisted() throws Exception{
        //createProfile postRequest

            String jsonString = """
    {
        "username": "ErdilWantsToQuit",
        "password": "securePassword123"
    }
""";

            String driverId = given()
                    .contentType("application/json")
                    .body(jsonString)
                    .when()
                    .post("/api/drivers")
                    .then()
                    .statusCode(201) //checking if statusCode is 201 Created
                    .extract()
                    .path("id");


            DriverProfile driverFoundFromDB = driverRepo.findById(UUID.fromString(driverId)).orElseThrow();
            assertEquals("ErdilWantsToQuit", driverFoundFromDB.getUsername());
        }
//post request update profile, check status code 200, check if updated in db
@Test
void updateProfile_withPutRequest_shouldReturn200_andUpdateDB() {

    DriverProfile driver = DriverProfile.createProfile("OldUsername", "oldPassword");
    driverRepo.save(driver);

    String driverId = driver.getId().toString();

    String jsonString = """
    {
      "username": "NewUsername",
      "password": "oldPassword"
    }
    """;

    given()
            .contentType("application/json")
            .auth().preemptive().basic("OldUsername", "oldPassword")
            .body(jsonString)
            .when()
            .put("/api/drivers/{id}", driverId)
            .then()
            .statusCode(200)
            .body("username", equalTo("NewUsername"));


    DriverProfile updated = driverRepo.findById(driver.getId()).orElseThrow();
    assertEquals("NewUsername", updated.getUsername());
}

@Test

void get_AllTripsofDriver_asSameUser_shouldReturn200_andTrips() {

    DriverProfile driver = DriverProfile.createProfile("TripUser", "tripPassword");
    driverRepo.save(driver);
    TripReport report1 = TripReport.newReport(driver.getId(), 120, 2, 3,
                    false,
                    java.time.LocalDateTime.of(2024, 5, 10, 14, 0),
                    java.time.LocalDateTime.of(2024, 5, 10, 15, 0)
            );
    reportRepo.save(report1);
    TripReport report2 = TripReport.newReport(driver.getId(), 120, 2, 3,
            false,
            java.time.LocalDateTime.of(2024, 5, 10, 14, 0),
            java.time.LocalDateTime.of(2024, 5, 10, 15, 0)
    );
    reportRepo.save(report2);
    TripReport report3 = TripReport.newReport(driver.getId(), 120, 2, 3,
            false,
            java.time.LocalDateTime.of(2024, 5, 10, 14, 0),
            java.time.LocalDateTime.of(2024, 5, 10, 15, 0)
    );
    reportRepo.save(report3);
    DriverProfile driverFromDB = null;

    Optional<DriverProfile> driverOpt = driverRepo.findById(driver.getId());
    if(driverOpt.isPresent()){
        driverFromDB = driverOpt.get();
    }
    System.out.println(driverFromDB.getTotalPoints());

    String driverId = driver.getId().toString();

    given()
            .auth().preemptive().basic("TripUser", "tripPassword")
            .when()
            .get("/api/trips/driver/{id}", driverId)
            .then()
            .statusCode(200);
}

}





