package de.thws.gamification.adapter.out.persistence.mapper;

import de.thws.gamification.adapter.out.persistence.entity.AchievementEntity;
import de.thws.gamification.adapter.out.persistence.entity.DriverAchievementEntity;
import de.thws.gamification.adapter.out.persistence.entity.DriverProfileEntity;
import de.thws.gamification.domain.model.DriverAchievement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;
@ApplicationScoped
public class DriverAchievementMapper {
    @Inject
    private AchievementMapper achievementMapper;

    //To Entity
    public DriverAchievementEntity toEntity(DriverAchievement driverAchievement, DriverProfileEntity driverProfile, AchievementEntity achievement){
        final var returnValue = new DriverAchievementEntity();
        returnValue.setId(driverAchievement.getId());
        returnValue.setDriver(driverProfile);
        returnValue.setAchievement(achievement);
        returnValue.setEarnedAt(driverAchievement.getEarnedAt());

        return returnValue;
    }

    public List<DriverAchievementEntity> toEntities(List<DriverAchievement> driverAchievements, DriverProfileEntity driverProfile, AchievementEntity achievement) {
        return driverAchievements.stream().
                map(da -> toEntity(da, driverProfile, achievement))
                .collect(Collectors.toList());
    }

    //To Domain

    public DriverAchievement toDomain(DriverAchievementEntity driverAchievementEntity){
        return new DriverAchievement(
                driverAchievementEntity.getId(),
                driverAchievementEntity.getDriver().getId(),
                achievementMapper.toDomain(driverAchievementEntity.getAchievement()),
                driverAchievementEntity.getEarnedAt()
        );

    }

    public List<DriverAchievement> toDomains(List<DriverAchievementEntity> driverAchievementEntities){
        return driverAchievementEntities.stream().map(this::toDomain).collect(Collectors.toList());
    }

}
