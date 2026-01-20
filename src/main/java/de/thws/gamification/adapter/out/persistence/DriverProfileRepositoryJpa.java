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
        // GÜNCELLEME Sadece 'DRIVER' rolüne sahip olanları getiriyoruz.
        final var driversFound = entityManager.createQuery(
                        "SELECT dp FROM DriverProfileEntity dp WHERE dp.role = 'DRIVER' ORDER BY dp.totalPoints DESC",
                        DriverProfileEntity.class)
                .getResultList();

        return mapper.toDomains(driversFound);
    }

    @Transactional
    @Override
    public DriverProfile save(DriverProfile driverProfile){
        final var entity = mapper.toEntity(driverProfile);
        entityManager.merge(entity);
        return mapper.toDomain(entity);
    }
    @Override
    public List<DriverProfile> searchDrivers(String usernameFilter, String roleFilter, Integer minScore) {

        StringBuilder jpql = new StringBuilder("SELECT d FROM DriverProfileEntity d WHERE 1=1");
        if (usernameFilter != null && !usernameFilter.isBlank()) {
            jpql.append(" AND LOWER(d.username) LIKE :username");
        }
        if (roleFilter != null && !roleFilter.isBlank()) {
            jpql.append(" AND d.role = :role");
        }
        if (minScore != null) {
            jpql.append(" AND d.totalPoints >= :minScore");
        }
        jpql.append(" ORDER BY d.totalPoints DESC");
        var query = entityManager.createQuery(jpql.toString(), DriverProfileEntity.class);
        if (usernameFilter != null && !usernameFilter.isBlank()) {
            query.setParameter("username", "%" + usernameFilter.toLowerCase() + "%");
        }
        if (roleFilter != null && !roleFilter.isBlank()) {
            query.setParameter("role", roleFilter.toUpperCase());
        }
        if (minScore != null) {
            query.setParameter("minScore", minScore);
        }
        return mapper.toDomains(query.getResultList());
    }

    @Transactional
    @Override
    public boolean deleteById(UUID id) {
        DriverProfileEntity entity = entityManager.find(DriverProfileEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
            return true;
        }
        return false;
    }

    @Override
    public Optional<DriverProfile> findByUsername(String username) {
        try {
            DriverProfileEntity entity = entityManager.createQuery(
                            "SELECT d FROM DriverProfileEntity d WHERE d.username = :username",
                            DriverProfileEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();

            return Optional.of(mapper.toDomain(entity));
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }


}
