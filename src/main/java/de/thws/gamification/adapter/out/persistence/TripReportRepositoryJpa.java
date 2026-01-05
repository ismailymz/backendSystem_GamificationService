package de.thws.gamification.adapter.out.persistence;

import de.thws.gamification.adapter.out.persistence.entity.DriverProfileEntity;
import de.thws.gamification.adapter.out.persistence.entity.TripReportEntity;
import de.thws.gamification.adapter.out.persistence.mapper.DriverProfileMapper;
import de.thws.gamification.adapter.out.persistence.mapper.TripReportMapper;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.TripReport;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@ApplicationScoped
public class TripReportRepositoryJpa implements TripReportRepository {
    @Inject
    TripReportMapper tripReportMapper;
    @Inject
    DriverProfileMapper driverProfileMapper;
    @Inject
    EntityManager em;

    @Transactional
    @Override
    public void save(TripReport report){
        DriverProfileEntity driverRef = em.getReference(DriverProfileEntity.class, report.getDriverId());
        final var reportEntity = tripReportMapper.toEntity(report, driverRef);
        em.persist(reportEntity);
    }
    @Override
    public Optional<TripReport> findById(UUID tripId){
        final var reportFound =
        em.find(TripReportEntity.class, tripId);
        return Optional.ofNullable(tripReportMapper.toDomain(reportFound));
    }
    @Override
    public List<TripReport> findByDriverId(UUID driverId){
    final var driversFound = em.createQuery("SELECT tr FROM TripReportEntity tr WHERE tr.driver.id = :driverId", TripReportEntity.class)
                .setParameter("driverId", driverId)
                .getResultList();
        return tripReportMapper.toDomains(driversFound);
    }
}
