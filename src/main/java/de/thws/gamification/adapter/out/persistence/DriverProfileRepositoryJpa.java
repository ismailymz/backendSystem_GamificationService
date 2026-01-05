package de.thws.gamification.adapter.out.persistence;

import de.thws.gamification.adapter.out.persistence.entity.DriverProfileEntity;
import de.thws.gamification.adapter.out.persistence.mapper.DriverProfileMapper;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.domain.model.DriverProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DriverProfileRepositoryJpa implements DriverProfileRepository {
@Inject
EntityManager entityManager;
@Inject
DriverProfileMapper mapper;

    @Override
    public Optional<DriverProfile> findById(UUID id){
        return Optional.ofNullable(mapper.toDomain(entityManager.find(DriverProfileEntity.class,id)));
    }

    @Override
    public List<DriverProfile> findAllOrderByTotalPointsDesc() {
        final var driversFound = entityManager.createQuery(
                "SELECT dp FROM DriverProfileEntity dp ORDER BY dp.totalPoints DESC",
                DriverProfileEntity.class)
                .getResultList();
        return mapper.toDomains(driversFound);
    }

    @Transactional
    @Override
    public DriverProfile save(DriverProfile driverProfile){
        final var entity = mapper.toEntity(driverProfile);
        entityManager.persist(entity);
        return mapper.toDomain(entity);
    }
}
