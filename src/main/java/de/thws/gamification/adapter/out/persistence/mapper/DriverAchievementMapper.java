package de.thws.gamification.adapter.out.persistence.mapper;

import de.thws.gamification.adapter.out.persistence.entity.AchievementEntity;
import de.thws.gamification.adapter.out.persistence.entity.DriverAchievementEntity;
import de.thws.gamification.adapter.out.persistence.entity.DriverProfileEntity;
import de.thws.gamification.domain.model.DriverAchievement;

public class DriverAchievementMapper {
    //To Entity
    public DriverAchievementEntity toEntity(DriverAchievement driverAchievement, DriverProfileEntity driverProfile, AchievementEntity achievement){
        final var returnValue = new DriverAchievementEntity();
        returnValue.setId(driverAchievement.getId());
        returnValue.setDriver(driverProfile);
        returnValue.setAchievement(achievement);
        returnValue.setEarnedAt(driverAchievement.getEarnedAt());

        return returnValue;
    }
}
