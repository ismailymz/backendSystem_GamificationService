package de.thws.gamification.adapter.out.persistence;

import de.thws.gamification.adapter.in.rest.util.CursorUtils.DecodedCursor;
import de.thws.gamification.adapter.out.persistence.entity.DriverProfileEntity;
import de.thws.gamification.adapter.out.persistence.entity.TripReportEntity;
import de.thws.gamification.adapter.out.persistence.mapper.TripReportMapper;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.LeaderboardEntry;
import de.thws.gamification.domain.model.TripReport;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TripReportRepositoryJpa implements TripReportRepository {

    @Inject
    TripReportMapper tripReportMapper;

    @Inject
    EntityManager em;

    @Override
    public List<LeaderboardEntry> getLeaderboardByDate(LocalDateTime fromDate) {
        // DİKKAT Artık SUM(tr.points) kullanıyoruz çünkü Entity'ye bu alanı ekledik
        String jpql = """
            SELECT dp.id, dp.username, SUM(tr.points) 
            FROM TripReportEntity tr
            JOIN tr.driver dp 
            WHERE tr.startedAt >= :fromDate
              AND dp.role = 'DRIVER'
            GROUP BY dp.id, dp.username
            ORDER BY SUM(tr.points) DESC
        """;

        List<Object[]> results = em.createQuery(jpql, Object[].class)
                .setParameter("fromDate", fromDate)
                .setMaxResults(10)
                .getResultList();

        return results.stream().map(row -> {
            UUID id = (UUID) row[0];
            String username = (String) row[1];
            // Eğer puan yoksa (null gelirse) 0 say, varsa int'e çevir
            int points = row[2] == null ? 0 : ((Number) row[2]).intValue();

            return new LeaderboardEntry(id, username, points, 1);
        }).toList();
    }

    @Transactional
    @Override
    public void save(TripReport report) {
        DriverProfileEntity driverRef = em.getReference(DriverProfileEntity.class, report.getDriverId());
        final var reportEntity = tripReportMapper.toEntity(report, driverRef);
        em.merge(reportEntity);
        em.flush();
    }

    @Override
    public Optional<TripReport> findById(UUID tripId){
        TripReportEntity found = em.find(TripReportEntity.class, tripId);
        if (found == null) return Optional.empty();
        return Optional.of(tripReportMapper.toDomain(found));
    }

    @Override
    public List<TripReport> findByDriverId(UUID driverId) {
        final var tripsFound = em.createQuery(
                        "SELECT tr FROM TripReportEntity tr WHERE tr.driver.id = :driverId",
                        TripReportEntity.class)
                .setParameter("driverId", driverId)
                .getResultList();
        return tripReportMapper.toDomains(tripsFound);
    }

    @Override
    public List<TripReport> findValidByDriverId(UUID driverId) {
        final var validTrips = em.createQuery(
                        "SELECT tr FROM TripReportEntity tr " +
                                "WHERE tr.driver.id = :driverId AND tr.voided = false",
                        TripReportEntity.class
                )
                .setParameter("driverId", driverId)
                .getResultList();

        return tripReportMapper.toDomains(validTrips);
    }

    @Override
    public List<TripReport> findLastTrips(UUID driverId, int limit) {
        return em.createQuery(
                        "SELECT t FROM TripReportEntity t " +
                                "WHERE t.driver.id = :driverId " +
                                "AND t.voided = false " +
                                "ORDER BY t.startedAt DESC, t.id DESC",
                        TripReportEntity.class)
                .setParameter("driverId", driverId)
                .setMaxResults(limit)
                .getResultList()
                .stream()
                .map(tripReportMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TripReport> findTripsByCursor(UUID driverId, int limit, DecodedCursor cursor) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM TripReportEntity t WHERE t.driver.id = :driverId");

        if (cursor != null) {
            jpql.append(" AND (t.startedAt < :cDate OR (t.startedAt = :cDate AND t.id < :cId))");
        }

        jpql.append(" ORDER BY t.startedAt DESC, t.id DESC");

        var query = em.createQuery(jpql.toString(), TripReportEntity.class);
        query.setParameter("driverId", driverId);

        if (cursor != null) {
            query.setParameter("cDate", cursor.date());
            query.setParameter("cId", cursor.id());
        }

        query.setMaxResults(limit);

        return query.getResultList().stream()
                .map(tripReportMapper::toDomain)
                .collect(Collectors.toList());
    }
}