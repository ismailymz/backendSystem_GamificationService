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

    @Override
    @Transactional
    public DriverProfile save(DriverProfile driverProfile) {
        // 1. Veritabanındaki GERÇEK ve DOLU kaydı çekiyoruz (Tripleri bilen kayıt)
        DriverProfileEntity entity = entityManager.find(DriverProfileEntity.class, driverProfile.getId());

        if (entity == null) {
            // Eğer ilk defa oluşturuluyorsa (Register), o zaman Mapper kullanabiliriz.
            entity = mapper.toEntity(driverProfile);
            entityManager.persist(entity);
        } else {
            // 2. Eğer kayıt varsa, listelere DOKUNMADAN sadece değişen alanları güncelliyoruz.
            entity.setTotalPoints(driverProfile.getTotalPoints());
            entity.setUsername(driverProfile.getUsername());
            entity.setRole(driverProfile.getRole());
            // Şifre değişecekse: entity.setPassword(driverProfile.getPassword());

            // DİKKAT: entity.setTripReports(...) ASLA YAPMIYORUZ!
        }

        // 3. flush() diyerek değişikliği garantiliyoruz.
        entityManager.flush();

        // 4. Güncel halini geri dönüyoruz
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
            entityManager.flush();
            entityManager.clear();
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
