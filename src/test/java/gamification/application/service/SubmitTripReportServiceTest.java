//package gamification.application.service; // Paket ismini ana kodunla aynı yaptım
//
//import de.thws.gamification.application.ports.out.DriverAchievementRepository;
//import de.thws.gamification.application.ports.out.DriverProfileRepository;
//import de.thws.gamification.application.ports.out.TripReportRepository;
//import de.thws.gamification.application.service.SubmitTripReportService;
//import de.thws.gamification.domain.model.DriverAchievement;
//import de.thws.gamification.domain.model.DriverProfile;
//import de.thws.gamification.domain.model.TripReport;
//import de.thws.gamification.domain.service.AchievementRule;
//import de.thws.gamification.domain.service.GamificationEngine;
//import de.thws.gamification.domain.service.GamificationResult;
//import de.thws.gamification.domain.service.ScoringPolicy;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//class SubmitTripReportServiceTest {
//
//    @Test
//    void submitTrip_shouldStoreTrip_UpdateDriverPoints_AndReturnResult() {
//        // Arrange
//        InMemoryDriverProfileRepository driverRepo = new InMemoryDriverProfileRepository();
//        InMemoryTripReportRepository tripRepo = new InMemoryTripReportRepository();
//        InMemoryDriverAchievementRepository achievementRepo = new InMemoryDriverAchievementRepository();
//
//        // Driver oluştur
//        DriverProfile driver = DriverProfile.createProfile("yusuf");
//        UUID driverId = driver.getId();
//        driverRepo.save(driver);
//
//        // TripReport oluştur
//        TripReport report = TripReport.newReport(
//                driverId,
//                20, 0, 0, false,
//                LocalDateTime.now(),
//                LocalDateTime.now().plusMinutes(15)
//        );
//
//        ScoringPolicy scoringPolicy = (trip, drv) -> 25;
//        AchievementRule rule = (trip, drv) -> Optional.empty();
//
//        GamificationEngine engine = new GamificationEngine(
//                List.of(scoringPolicy),
//                List.of(rule)
//        );
//
//        // --- DÜZELTME BURADA: PARAMETRE SIRALAMASI DEĞİŞTİ ---
//        // Önce tripRepo, Sonra driverRepo gelmeli (Servis'teki sıraya bakarak)
//        SubmitTripReportService service = new SubmitTripReportService(
//                tripRepo,       // 1. Parametre
//                driverRepo,     // 2. Parametre
//                achievementRepo,// 3. Parametre
//                engine          // 4. Parametre
//        );
//        // -----------------------------------------------------
//
//        // Act
//        GamificationResult result = service.submitTrip(driverId, report);
//
//        // Assert
//        assertEquals(25, result.getPointsAdded(), "Eklenen puan 25 olmalı");
//
//        DriverProfile updatedDriver = driverRepo.findById(driverId)
//                .orElseThrow(() -> new AssertionError("Driver bulunamadı"));
//        assertEquals(25, updatedDriver.getTotalPoints(), "Driver toplam puanı 25 olmalı");
//
//        List<TripReport> driverTrips = tripRepo.findByDriverId(driverId);
//        assertEquals(1, driverTrips.size(), "Driver'ın 1 trip'i olmalı");
//        assertEquals(report.getId(), driverTrips.get(0).getId(), "Kaydedilen trip aynı olmalı");
//
//        assertTrue(achievementRepo.savedAchievements.isEmpty(), "Achievement kaydedilmemeli");
//    }
//
//    // --- InMemory Implementasyonlar ---
//
//    static class InMemoryDriverProfileRepository implements DriverProfileRepository {
//        private final Map<UUID, DriverProfile> store = new HashMap<>();
//
//        @Override
//        public Optional<DriverProfile> findById(UUID id) {
//            return Optional.ofNullable(store.get(id));
//        }
//
//        @Override // Interface'de bu metot olmayabilir ama test için kalsın
//        public List<DriverProfile> findAllOrderByTotalPointsDesc() {
//            return store.values().stream()
//                    .sorted(Comparator.comparingInt(DriverProfile::getTotalPoints).reversed())
//                    .toList();
//        }
//
//        @Override
//        public DriverProfile save(DriverProfile driverProfile) {
//            store.put(driverProfile.getId(), driverProfile);
//            return driverProfile;
//        }
//    }
//
//    static class InMemoryTripReportRepository implements TripReportRepository {
//        private final Map<UUID, TripReport> store = new HashMap<>();
//
//        @Override
//        public void save(TripReport report) {
//            store.put(report.getId(), report);
//        }
//
//        @Override
//        public Optional<TripReport> findById(UUID id) {
//            return Optional.ofNullable(store.get(id));
//        }
//
//        @Override
//        public List<TripReport> findByDriverId(UUID driverId) {
//            return store.values().stream()
//                    .filter(r -> r.getDriverId().equals(driverId))
//                    .toList();
//        }
//
//        @Override
//        public List<TripReport> findValidByDriverId(UUID driverId) {
//            return store.values().stream()
//                    .filter(r -> r.getDriverId().equals(driverId))
//                    .filter(r -> !r.isVoided())
//                    .toList();
//        }
//    }
//
//    static class InMemoryDriverAchievementRepository implements DriverAchievementRepository {
//        final List<DriverAchievement> savedAchievements = new ArrayList<>();
//
//        @Override
//        public List<DriverAchievement> findByDriverId(UUID driverId) {
//            return savedAchievements.stream()
//                    .filter(a -> a.getDriverId().equals(driverId))
//                    .toList();
//        }
//
//        @Override
//        public void deleteByDriverId(UUID driverId) {
//            savedAchievements.removeIf(a -> a.getDriverId().equals(driverId));
//        }
//
//
//        @Override
//        public void saveAll(List<DriverAchievement> achievements) {
//            savedAchievements.addAll(achievements);
//        }
//    }
//}