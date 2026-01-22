package gamification.adapter.out.persistence.jpaAdapterTest;

import de.thws.gamification.adapter.in.rest.util.CursorUtils;
import de.thws.gamification.adapter.out.persistence.mapper.DriverProfileMapper;
import de.thws.gamification.adapter.out.persistence.mapper.TripReportMapper;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.LeaderboardEntry;
import de.thws.gamification.domain.model.TripReport;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestTransaction
public class TripReportJpaTest {

    @Inject
    DriverProfileRepository driverRepo;

    @Inject
    TripReportRepository reportRepo;



//save, findById, findByDriverId +
// findValidByDriverId, findLastTrips,

//public static TripReport newReport(UUID driverId, int distanceKm, int brakeCount, int hardAccelerationCount, boolean nightTrip, LocalDateTime startedAt, LocalDateTime endedAt)
//TripReport report = TripReport.newReport(driver.getId(), 110, 3,4,true, LocalDateTime.of(2026,1,22,9,10), LocalDateTime.of(2026,1,22,9,50));
    @Test
    void persistTripReportAndFindByIdAndFindByDriverId(){
        DriverProfile driver = DriverProfile.createProfile("Erdil", "erdil123");
        driverRepo.save(driver);
        TripReport trip = TripReport.newReport(driver.getId(), 110, 3,4,true, LocalDateTime.of(2026,1,22,9,10), LocalDateTime.of(2026,1,22,9,50));
        reportRepo.save(trip);
        TripReport tripFoundById;
        Optional<TripReport> tripOpt =reportRepo.findById(trip.getId());
        if(tripOpt.isPresent()){
            tripFoundById = tripOpt.get();
        }
        else{
            throw new AssertionError("Trip not found!");
        }

        List<TripReport> tripDriverOpt =reportRepo.findByDriverId(trip.getDriverId());
        TripReport tripFoundByDriverId = tripDriverOpt.getFirst();

        assertEquals(trip.getId(), tripFoundByDriverId.getId());
        assertEquals(trip.getDriverId(), tripFoundByDriverId.getDriverId());
        assertEquals(trip.getDistanceKm(), tripFoundByDriverId.getDistanceKm());
        assertEquals(trip.getBrakeCount(), tripFoundByDriverId.getBrakeCount());
        assertEquals(trip.getHardAccelerationCount(), tripFoundByDriverId.getHardAccelerationCount());
        assertEquals(trip.isNightTrip(), tripFoundByDriverId.isNightTrip());
        assertEquals(trip.getStartedAt(), tripFoundByDriverId.getStartedAt());
        assertEquals(trip.getEndedAt(), tripFoundByDriverId.getEndedAt());


        assertEquals(trip.getId(), tripFoundById.getId());
        assertEquals(trip.getDriverId(), tripFoundById.getDriverId());
        assertEquals(trip.getDistanceKm(), tripFoundById.getDistanceKm());
        assertEquals(trip.getBrakeCount(), tripFoundById.getBrakeCount());
        assertEquals(trip.getHardAccelerationCount(), tripFoundById.getHardAccelerationCount());
        assertEquals(trip.isNightTrip(), tripFoundById.isNightTrip());
        assertEquals(trip.getStartedAt(), tripFoundById.getStartedAt());
        assertEquals(trip.getEndedAt(), tripFoundById.getEndedAt());
    }
    @Test
    void findDriversValidTripsById(){
        DriverProfile driver = DriverProfile.createProfile("Erdil", "erdil123");
        UUID id = driver.getId();
        driverRepo.save(driver);

        TripReport trip = TripReport.newReport(driver.getId(), 110, 3,4,true, LocalDateTime.of(2026,1,22,9,10), LocalDateTime.of(2026,1,22,9,50));
        trip.markVoided();

        reportRepo.save(trip);

        TripReport trip1 = TripReport.newReport(driver.getId(), 56, 3,6,true, LocalDateTime.of(2026,1,22,9,10), LocalDateTime.of(2026,1,22,9,50));
        reportRepo.save(trip1);

        TripReport trip2 = TripReport.newReport(driver.getId(), 110, 3,4,true, LocalDateTime.of(2026,1,22,9,10), LocalDateTime.of(2026,1,22,9,50));
        reportRepo.save(trip2);


        TripReport trip3 = TripReport.newReport(driver.getId(), 110, 3,4,true, LocalDateTime.of(2026,1,22,9,10), LocalDateTime.of(2026,1,22,9,50));
       trip3.markVoided();
        reportRepo.save(trip3);

        List<TripReport> tripsFound = reportRepo.findValidByDriverId(id);

        assertEquals(trip1.getId(),tripsFound.getFirst().getId());
        assertEquals(trip2.getId(),tripsFound.get(1).getId());

    }
    //Started to write with java test name-standards -E.K
@Test
    void findLastTripsByDriverId_whenLimitIsGiven_returnsLimitedTrips(){
    DriverProfile driver = DriverProfile.createProfile("Erdil", "erdil123");
    UUID id = driver.getId();
    driverRepo.save(driver);

    TripReport trip = TripReport.newReport(driver.getId(), 110, 3,4,true, LocalDateTime.of(2026,1,22,9,10), LocalDateTime.of(2026,1,22,9,50));


    reportRepo.save(trip);

    TripReport trip1 = TripReport.newReport(driver.getId(), 56, 3,6,true, LocalDateTime.of(2025,1,22,9,10), LocalDateTime.of(2026,1,22,9,50));
    reportRepo.save(trip1);

    TripReport trip2 = TripReport.newReport(driver.getId(), 110, 3,4,true, LocalDateTime.of(2024,1,22,9,10), LocalDateTime.of(2026,1,22,9,50));
    reportRepo.save(trip2);


    TripReport trip3 = TripReport.newReport(driver.getId(), 23, 3,4,true, LocalDateTime.of(2026,2,22,11,10), LocalDateTime.of(2027,1,22,12,45));

    reportRepo.save(trip3);

    // ordered ->trip 3,0,1,2

    List<TripReport> listFoundLimit4 = reportRepo.findLastTrips(driver.getId(), 4);
    List<TripReport> listFoundLimit2= reportRepo.findLastTrips(driver.getId(), 2);

    List<TripReport> listExpectedLimit4 = new ArrayList<>();
    listExpectedLimit4.add(trip3);
    listExpectedLimit4.add(trip);
    listExpectedLimit4.add(trip1);
    listExpectedLimit4.add(trip2);

    List<TripReport> listExpectedLimit2 = new ArrayList<>();
    listExpectedLimit2.add(trip3);
    listExpectedLimit2.add(trip);


    for(int i = 0; i<listExpectedLimit4.size(); i++){
        assertEquals(listExpectedLimit4.get(i).getId(), listFoundLimit4.get(i).getId());
    }
        for(int i = 0; i<listExpectedLimit2.size(); i++){
           assertEquals(listExpectedLimit2.get(i).getId(), listFoundLimit2.get(i).getId());
        }

    }
//    List<TripReport> findTripsByCursor(UUID driverId, int limit, CursorUtils.DecodedCursor cursor);
//    List<LeaderboardEntry> getLeaderboardByDate(LocalDateTime fromDate);



}
