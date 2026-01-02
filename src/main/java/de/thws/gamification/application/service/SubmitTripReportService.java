package de.thws.gamification.application.service;

import de.thws.gamification.application.ports.in.SubmitTripReportUseCase;
import de.thws.gamification.application.ports.out.DriverAchievementRepository;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.DriverAchievement;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.GamificationEngine;
import de.thws.gamification.domain.service.GamificationResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class SubmitTripReportService implements SubmitTripReportUseCase {

    private final DriverProfileRepository driverProfileRepository;
    private final TripReportRepository tripReportRepository;
    private final DriverAchievementRepository driverAchievementRepository;
    private final GamificationEngine gamificationEngine;

    @Inject
    public SubmitTripReportService(DriverProfileRepository driverProfileRepository,
                                   TripReportRepository tripReportRepository,
                                   DriverAchievementRepository driverAchievementRepository,
                                   GamificationEngine gamificationEngine) {
        this.driverProfileRepository = driverProfileRepository;
        this.tripReportRepository = tripReportRepository;
        this.driverAchievementRepository = driverAchievementRepository;
        this.gamificationEngine = gamificationEngine;
    }

    @Override
    public GamificationResult submitTrip(UUID driverId, TripReport report) {

        if (driverId == null) {
            throw new IllegalArgumentException("driverId must not be null");
        }
        if (report == null) {
            throw new IllegalArgumentException("report must not be null");
        }
        if (report.getDriverId() == null) {
            throw new IllegalArgumentException("report.driverId must not be null");
        }
        if (!driverId.equals(report.getDriverId())) {
            throw new IllegalArgumentException("driverId and report.driverId must match");
        }

        // 1) driver'ı repository'den çek
        DriverProfile driver = driverProfileRepository.findById(driverId)
                .orElseThrow(() -> new NoSuchElementException("Driver not found"));

        // 2) trip'i kaydet
        tripReportRepository.save(report);

        // 3) domain engine'i çağır (puan + achievement hesap)
        GamificationResult result = gamificationEngine.applyTrip(driver, report);

        // 4) güncellenmiş driver'ı kaydet (points vs.)
        DriverProfile updatedDriver = result.getUpdatedDriver();
        driverProfileRepository.save(updatedDriver);

        // 5) yeni achievement'ları driverAchievement olarak persist et
        if (!result.getNewAchievements().isEmpty()) {
            driverAchievementRepository.saveAll(result.getNewAchievements());
        }


        return result;
    }
}
