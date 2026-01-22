package gamification.integration;

import de.thws.gamification.application.ports.out.AchievementRepository;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.application.service.RetrieveLeaderboardService;
import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.LeaderboardEntry;
import de.thws.gamification.domain.model.Period;
import de.thws.gamification.domain.model.TripReport;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import io.quarkus.cache.CacheManager;
import org.junit.jupiter.api.BeforeEach;
import jakarta.inject.Inject;



import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RetrieveLeaderboardServiceIT {

    private static final UUID SAFE_DRIVER_ID  = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID NIGHT_DRIVER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    @Inject
    CacheManager cacheManager;
    @BeforeEach
    void clearLeaderboardCache() {
        cacheManager.getCache("leaderboard")
                .ifPresent(cache ->
                        cache.invalidateAll().await().indefinitely()
                );
    } //Cleaning cache before each test to avoid stale data issues. Otherwise, the 4th test fails. -E.K.


    @Inject
    RetrieveLeaderboardService leaderboardService;

    @Inject DriverProfileRepository driverRepo;
    @Inject TripReportRepository tripRepo;
    @Inject AchievementRepository achievementRepo;

    private void seedAchievementsIfMissing() {
        if (achievementRepo.findByCode("SAFE_DRIVER").isEmpty()) {
            achievementRepo.save(new Achievement(
                    SAFE_DRIVER_ID,
                    "SAFE_DRIVER",
                    "Unbreakable",
                    "Completed 5 trips without hard brakes!"
            ));
        }
        if (achievementRepo.findByCode("NIGHT_DRIVER").isEmpty()) {
            achievementRepo.save(new Achievement(
                    NIGHT_DRIVER_ID,
                    "NIGHT_DRIVER",
                    "Night Owl",
                    "Completed 3 trips between 22:00 and 05:00"
            ));
        }
    }

    @Test
    @TestTransaction
    void getLeaderboard_allTime_returnsRankedEntries() {

        seedAchievementsIfMissing();

        DriverProfile d1 = DriverProfile.createProfile("user1", "pw");
        DriverProfile d2 = DriverProfile.createProfile("user2", "pw");
        DriverProfile d3 = DriverProfile.createProfile("user3", "pw");

        d1.addPoints(50);
        d2.addPoints(30);
        d3.addPoints(20);

        driverRepo.save(d1);
        driverRepo.save(d2);
        driverRepo.save(d3);


        List<LeaderboardEntry> result = leaderboardService.getLeaderboard(Period.ALL_TIME);


        assertEquals(3, result.size());

        assertEquals(1, result.get(0).getRank());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals(50, result.get(0).getPoints());

        assertEquals(2, result.get(1).getRank());
        assertEquals("user2", result.get(1).getUsername());
        assertEquals(30, result.get(1).getPoints());

        assertEquals(3, result.get(2).getRank());
        assertEquals("user3", result.get(2).getUsername());
        assertEquals(20, result.get(2).getPoints());
    }

    @Test
    @TestTransaction
    void getLeaderboard_daily_returnsRankedEntries_fromTrips() {

        seedAchievementsIfMissing();

        DriverProfile u1 = DriverProfile.createProfile("user1", "pw");
        DriverProfile u2 = DriverProfile.createProfile("user2", "pw");
        driverRepo.save(u1);
        driverRepo.save(u2);


        LocalDateTime withinLastDay = LocalDateTime.now().minusHours(2);

        // user1 total 40 puan
        TripReport t1 = TripReport.newReport(u1.getId(), 1, 0, 0, true, withinLastDay.minusMinutes(30), withinLastDay.minusMinutes(10));
        TripReport t2 = TripReport.newReport(u1.getId(), 1, 0, 0, true, withinLastDay.minusMinutes(20), withinLastDay.minusMinutes(5));
        t1.setTotalScore(25);
        t2.setTotalScore(15);
        tripRepo.save(t1);
        tripRepo.save(t2);

        // user2 total 20 puan
        TripReport t3 = TripReport.newReport(u2.getId(), 1, 0, 0, true, withinLastDay.minusMinutes(15), withinLastDay.minusMinutes(1));
        t3.setTotalScore(20);
        tripRepo.save(t3);


        List<LeaderboardEntry> result = leaderboardService.getLeaderboard(Period.DAILY);


        assertEquals(2, result.size());

        assertEquals(1, result.get(0).getRank());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals(40, result.get(0).getPoints());

        assertEquals(2, result.get(1).getRank());
        assertEquals("user2", result.get(1).getUsername());
        assertEquals(20, result.get(1).getPoints());
    }

    @Test
    @TestTransaction
    void getLeaderboard_weekly_returnsRankedEntries_fromTrips() {

        seedAchievementsIfMissing();

        DriverProfile u1 = DriverProfile.createProfile("user1", "pw");
        DriverProfile u3 = DriverProfile.createProfile("user3", "pw");
        driverRepo.save(u1);
        driverRepo.save(u3);

        // WEEKLY: fromDate = now - 7 days
        LocalDateTime withinLastWeek = LocalDateTime.now().minusDays(2);

        // user1 total 70
        TripReport a = TripReport.newReport(u1.getId(), 1, 0, 0, true, withinLastWeek.minusHours(2), withinLastWeek.minusHours(1));
        TripReport b = TripReport.newReport(u1.getId(), 1, 0, 0, true, withinLastWeek.minusHours(1), withinLastWeek.minusMinutes(10));
        a.setTotalScore(40);
        b.setTotalScore(30);
        tripRepo.save(a);
        tripRepo.save(b);

        // user3 total 50
        TripReport c = TripReport.newReport(u3.getId(), 1, 0, 0, true, withinLastWeek.minusHours(3), withinLastWeek.minusHours(2));
        c.setTotalScore(50);
        tripRepo.save(c);


        List<LeaderboardEntry> result = leaderboardService.getLeaderboard(Period.WEEKLY);


        assertEquals(2, result.size());

        assertEquals(1, result.get(0).getRank());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals(70, result.get(0).getPoints());

        assertEquals(2, result.get(1).getRank());
        assertEquals("user3", result.get(1).getUsername());
        assertEquals(50, result.get(1).getPoints());
    }

    @Test
    @TestTransaction
    void getLeaderboard_allTime_emptyResult_returnsEmptyLeaderboard() {

        seedAchievementsIfMissing();

        List<LeaderboardEntry> result = leaderboardService.getLeaderboard(Period.ALL_TIME);


        assertTrue(result.isEmpty());
    }
}
