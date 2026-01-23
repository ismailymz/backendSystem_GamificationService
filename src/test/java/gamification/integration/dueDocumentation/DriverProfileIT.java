package gamification.integration.dueDocumentation;

import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.domain.model.DriverProfile;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

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

    }





