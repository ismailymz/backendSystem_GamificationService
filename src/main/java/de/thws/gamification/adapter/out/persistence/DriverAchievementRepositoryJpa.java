package de.thws.gamification.adapter.out.persistence;

import de.thws.gamification.adapter.out.persistence.entity.AchievementEntity;
import de.thws.gamification.adapter.out.persistence.entity.DriverAchievementEntity;
import de.thws.gamification.adapter.out.persistence.entity.DriverProfileEntity;
import de.thws.gamification.adapter.out.persistence.mapper.DriverAchievementMapper;
import de.thws.gamification.application.ports.out.DriverAchievementRepository;
import de.thws.gamification.domain.model.DriverAchievement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.UUID;
@ApplicationScoped
public class DriverAchievementRepositoryJpa implements DriverAchievementRepository {
    @Inject
    EntityManager entityManager;
    @Inject
    DriverAchievementMapper mapper;


    @Override
    public List<DriverAchievement> findByDriverId(UUID driverId){

        final var driversFound = entityManager.createQuery(
                   "SELECT da FROM DriverAchievementEntity da WHERE da.driver.id = :driverId",
                           DriverAchievementEntity.class)
                   .setParameter("driverId", driverId)
                   .getResultList();
        if(driversFound==null){
            throw new NotFoundException("Driver not found: "+ driverId);
        }
           return mapper.toDomains(driversFound);
    }

@Transactional //Do it completely or not at all. - Erdil
    @Override
    public void saveAll(List<DriverAchievement> achievements){
        if (achievements == null || achievements.isEmpty()) return;
        for(DriverAchievement da: achievements){
            DriverProfileEntity driverRef =
                    entityManager.getReference(DriverProfileEntity.class, da.getDriverId());

            AchievementEntity achRef =
                    entityManager.getReference(AchievementEntity.class, da.getAchievement().getId());

            DriverAchievementEntity entity = mapper.toEntity(da, driverRef, achRef);
            entityManager.persist(entity);
        }
    }
}
