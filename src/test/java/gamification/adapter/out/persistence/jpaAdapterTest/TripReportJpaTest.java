package gamification.adapter.out.persistence.jpaAdapterTest;

import de.thws.gamification.adapter.out.persistence.mapper.DriverProfileMapper;
import de.thws.gamification.adapter.out.persistence.mapper.TripReportMapper;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestTransaction
public class TripReportJpaTest {
    @Inject
    DriverProfileMapper driverMapper;
    @Inject
    DriverProfileRepository driverRepo;
    @Inject
    TripReportMapper reportMapper;
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



}
