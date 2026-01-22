package gamification.integration.dueDocumentation;


import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
class SubmitTripReportIT {

    @Inject
    DriverProfileRepository driverRepo;

    @Inject
    TripReportRepository tripRepo;

    UUID driverId;
    String authHeader;





}

