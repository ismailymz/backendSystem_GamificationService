package de.thws.gamification.application.service;

import de.thws.gamification.application.ports.in.VoidTripUseCase;
import de.thws.gamification.application.ports.out.DriverAchievementRepository;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.GamificationEngine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;
@ApplicationScoped
public class VoidTripService implements VoidTripUseCase {

    private final TripReportRepository tripReportRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final DriverAchievementRepository driverAchievementRepository;
    private final GamificationEngine gamificationEngine;

   @Inject
    public VoidTripService(TripReportRepository tripReportRepository, DriverProfileRepository driverProfileRepository,DriverAchievementRepository driverAchievementRepository,GamificationEngine gamificationEngine) {
        this.tripReportRepository = tripReportRepository;
        this.driverProfileRepository = driverProfileRepository;
        this.driverAchievementRepository= driverAchievementRepository;
        this.gamificationEngine=gamificationEngine;
    }

    @Override
    @Transactional
    public void voidTrip(UUID tripId) {

        if (tripId == null) {
            throw new IllegalArgumentException("Trip Id can't be null");
        }

        TripReport report = tripReportRepository.findById(tripId)
                .orElseThrow(() ->
                        new NoSuchElementException("Trip Not found" + tripId));

        //idempotent
        if (report.isVoided()) {
            return;
        }

        report.markVoided();
        tripReportRepository.save(report);

        var driver = driverProfileRepository.findById(report.getDriverId())
                .orElseThrow(() ->
                        new NoSuchElementException("Driver not found"));

        //Driver state reset
        driver.resetPoints();
        driver.clearAchievements();
        driverAchievementRepository.deleteByDriverId(driver.getId());

        var validTrips =
                tripReportRepository.findValidByDriverId(driver.getId());

        for (TripReport t : validTrips) {
            gamificationEngine.applyTrip(driver, t);
        }

        driverProfileRepository.save(driver);
    }


}
