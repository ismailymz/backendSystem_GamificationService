package de.thws.gamification.application.service;

import de.thws.gamification.application.ports.in.SubmitTripReportUseCase;
import de.thws.gamification.application.ports.out.DriverAchievementRepository;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.GamificationEngine;
import de.thws.gamification.domain.service.GamificationResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.Collections;
import java.util.UUID;

@ApplicationScoped
public class SubmitTripReportService implements SubmitTripReportUseCase {

    private final TripReportRepository tripRepo;
    private final DriverProfileRepository driverRepo;
    private final DriverAchievementRepository achievementRepo;
    private final GamificationEngine engine;

    @Inject
    public SubmitTripReportService(TripReportRepository tripRepo,
                                   DriverProfileRepository driverRepo,
                                   DriverAchievementRepository achievementRepo,
                                   GamificationEngine engine) {
        this.tripRepo = tripRepo;
        this.driverRepo = driverRepo;
        this.achievementRepo = achievementRepo;
        this.engine = engine;
    }

    @Override
    @Transactional
    public GamificationResult submitTrip(UUID driverId, TripReport report) {

        DriverProfile driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new NotFoundException("Driver not found with ID: " + driverId));

        //Idempotency
        if (tripRepo.findById(report.getId()).isPresent()) {
            return new GamificationResult(driver, 0, Collections.emptyList());
        }
        GamificationResult result = engine.applyTrip(driver, report);
        report.setTotalScore(result.getPointsAdded());
        tripRepo.save(report);
        driverRepo.save(result.getUpdatedDriver());
        if (!result.getNewAchievements().isEmpty()) {
            achievementRepo.saveAll(result.getNewAchievements());
        }
        return result;
    }
}