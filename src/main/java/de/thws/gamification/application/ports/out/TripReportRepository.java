package de.thws.gamification.application.ports.out;

import de.thws.gamification.adapter.in.rest.util.CursorUtils.DecodedCursor;
import de.thws.gamification.domain.model.LeaderboardEntry;
import de.thws.gamification.domain.model.TripReport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripReportRepository {
    void save(TripReport tripReport);
    Optional<TripReport> findById(UUID tripId);
    List<TripReport> findByDriverId(UUID driverId);
    List<TripReport> findValidByDriverId(UUID driverId);
    // Kural motoru için son N sürüşü getiren metot
    List<TripReport> findLastTrips(UUID driverId, int limit);
    // Pagination için Cursor metodu
    List<TripReport> findTripsByCursor(UUID driverId, int limit, DecodedCursor cursor);
    List<LeaderboardEntry> getLeaderboardByDate(LocalDateTime fromDate);
}
