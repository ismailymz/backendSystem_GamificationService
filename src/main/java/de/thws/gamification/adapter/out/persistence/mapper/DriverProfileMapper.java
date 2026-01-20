package de.thws.gamification.adapter.out.persistence.mapper;

import de.thws.gamification.adapter.out.persistence.entity.AchievementEntity;
import de.thws.gamification.adapter.out.persistence.entity.DriverAchievementEntity;
import de.thws.gamification.adapter.out.persistence.entity.DriverProfileEntity;
import de.thws.gamification.domain.model.DriverAchievement;
import de.thws.gamification.domain.model.DriverProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DriverProfileMapper {

    @Inject
    EntityManager em;

    @Inject
    AchievementMapper achievementMapper;

    public DriverProfileEntity toEntity(DriverProfile domain){
        if (domain == null) return null;

        final var entity = new DriverProfileEntity();
        entity.setId(domain.getId());
        entity.setUsername(domain.getUsername());
        entity.setTotalPoints(domain.getTotalPoints());
        entity.setPassword(domain.getPassword());
        entity.setRole(domain.getRole());


        List<DriverAchievementEntity> achievementEntities = new ArrayList<>();

        if (domain.getEarnedAchievements() != null) {
            for (DriverAchievement daDomain : domain.getEarnedAchievements()) {
                DriverAchievementEntity daEntity = new DriverAchievementEntity();

                daEntity.setId(daDomain.getId());
                daEntity.setEarnedAt(daDomain.getEarnedAt());
                daEntity.setDriver(entity);

                AchievementEntity ref = em.getReference(AchievementEntity.class, daDomain.getAchievement().getId());
                daEntity.setAchievement(ref);

                achievementEntities.add(daEntity);
            }
        }

        entity.setAchievements(achievementEntities);

        return entity;
    }



    public List<DriverProfileEntity> toEntities(List<DriverProfile> driverProfiles){
        return driverProfiles.stream().map(this::toEntity).collect(Collectors.toList());
    }


    public DriverProfile toDomain(DriverProfileEntity entity){
        if (entity == null) return null;


        DriverProfile profile = new DriverProfile(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(), // Şifre (Auth için lazım)
                entity.getRole(),     // Rol (Admin kontrolü için lazım)
                entity.getTotalPoints()
        );


        if (entity.getAchievements() != null) {
            for (DriverAchievementEntity daEntity : entity.getAchievements()) {
                DriverAchievement daDomain = new DriverAchievement(
                        daEntity.getId(),
                        entity.getId(),
                        achievementMapper.toDomain(daEntity.getAchievement()),
                        daEntity.getEarnedAt()
                );
                profile.addAchievement(daDomain);
            }
        }

        return profile;
    }

    public List<DriverProfile> toDomains(List<DriverProfileEntity> driverProfileEntities){
        return driverProfileEntities.stream().map(this::toDomain).collect(Collectors.toList());
    }
}