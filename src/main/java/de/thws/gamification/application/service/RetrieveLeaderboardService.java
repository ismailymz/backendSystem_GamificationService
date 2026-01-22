package de.thws.gamification.application.service;

import de.thws.gamification.application.ports.in.RetrieveLeaderboardQuery;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.LeaderboardEntry;
import de.thws.gamification.domain.model.Period;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RetrieveLeaderboardService implements RetrieveLeaderboardQuery {

    private final DriverProfileRepository driverProfileRepository;
    private final TripReportRepository tripReportRepository;

    @Inject
    public RetrieveLeaderboardService(DriverProfileRepository driverProfileRepository,
                                      TripReportRepository tripReportRepository) {
        this.driverProfileRepository = driverProfileRepository;
        this.tripReportRepository = tripReportRepository;
    }

    @Override
    @CacheInvalidateAll(cacheName = "leaderboard")
    public List<LeaderboardEntry> getLeaderboard(Period period) {

        List<LeaderboardEntry> rawList;

        if (period == Period.ALL_TIME) {

            List<DriverProfile> drivers = driverProfileRepository.findAllOrderByTotalPointsDesc();


            rawList = drivers.stream()
                    .map(d -> new LeaderboardEntry(
                            d.getId(),
                            d.getUsername(),
                            (int) d.getTotalPoints(), // Long -> int dönüşümü gerekebilir
                            1)) // Geçici rank (Validation hatası yememek için 1 veriyoruz)
                    .toList();

        } else {
            LocalDateTime fromDate = calculateStartDate(period);
            rawList = tripReportRepository.getLeaderboardByDate(fromDate);
        }

        List<LeaderboardEntry> finalLeaderboard = new ArrayList<>();
        int rank = 1;

        for (LeaderboardEntry entry : rawList) {
            finalLeaderboard.add(new LeaderboardEntry(
                    entry.getDriverId(),
                    entry.getUsername(),
                    entry.getPoints(),
                    rank++
            ));
        }

        return finalLeaderboard;
    }

    private LocalDateTime calculateStartDate(Period period) {
        LocalDateTime now = LocalDateTime.now();
        if (period == Period.DAILY) {
            return now.minusDays(1);
        } else if (period == Period.WEEKLY) {
            return now.minusDays(7);
        }
        return now.minusYears(100);
    }
}