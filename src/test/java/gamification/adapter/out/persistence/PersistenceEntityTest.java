package gamification.adapter.out.persistence;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import de.thws.gamification.adapter.out.persistence.entity.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PersistenceEntityTest {

    @Inject
    EntityManager em;

    @Test
    @TestTransaction
    void persist_tripReport_with_driverProfile_creates_fk_and_can_be_loaded() {
        //Controlling if driver and trip report persistence with the relationship works

        UUID driverId = UUID.randomUUID();
        DriverProfileEntity driver = new DriverProfileEntity();
        driver.setId(driverId);
        driver.setUsername("Erdil");
        driver.setTotalPoints(100);

        UUID reportId = UUID.randomUUID();
        TripReportEntity report = new TripReportEntity();
        report.setId(reportId);
        report.setDistanceKm(12);
        report.setBrakeCount(1);
        report.setHardAccelerationCount(2);
        report.setNightTrip(true);


        report.setDriver(driver);

        // when
        em.persist(driver);
        em.persist(report);
        em.flush();
        em.clear();

        // then
        TripReportEntity loaded = em.find(TripReportEntity.class, reportId);
        assertNotNull(loaded);

        assertNotNull(loaded.getDriver());
        assertEquals(driverId, loaded.getDriver().getId());

        assertEquals(12, loaded.getDistanceKm());
        assertTrue(loaded.isNightTrip());
    }

    @Test
    @TestTransaction
    void persist_tripReport_without_driver_should_fail_if_not_nullable() {

        //Trip report without the driver must not be persisted
        TripReportEntity report = new TripReportEntity();
        report.setId(UUID.randomUUID());

        assertThrows(Exception.class, () -> {
            em.persist(report);
            em.flush();
        });
    }
}