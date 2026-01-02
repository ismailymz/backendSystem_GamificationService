package de.thws.gamification.application.service;

import de.thws.gamification.application.ports.in.RetrieveLeaderboardQuery;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.LeaderboardEntry;
import de.thws.gamification.domain.model.Period;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
@ApplicationScoped
public class RetrieveLeaderboardService implements RetrieveLeaderboardQuery  {

    private final DriverProfileRepository driverProfileRepository;
@Inject
    public RetrieveLeaderboardService(DriverProfileRepository driverProfileRepository) {
        this.driverProfileRepository = driverProfileRepository;
    }

    @Override
    public List<LeaderboardEntry> getLeaderboard(Period period) {
        // Şu an domain'de period'e göre farklı hesaplama yok,
        // o yüzden period parametresini sadece gelecekte kullanmak için alıyoruz.

        // Driver'ları toplam puana göre sıralı al
        List<DriverProfile> sortedDrivers = driverProfileRepository.findAllOrderByTotalPointsDesc();

        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        int rank = 1;

        for (DriverProfile driver : sortedDrivers) {
            leaderboard.add(new LeaderboardEntry(
                    driver.getId(),
                    driver.getUsername(),
                    driver.getTotalPoints(),
                    rank++
            ));
        }

        return leaderboard;
    }
    }

