package de.thws.gamification.adapter.out.persistence.mapper;
import de.thws.gamification.adapter.out.persistence.entity.AchievementEntity;
import de.thws.gamification.domain.model.Achievement;

import java.util.List;
import java.util.stream.Collectors;

public class AchievementMapper {

    //to Entity
    public AchievementEntity toEntity(Achievement achievement){
        final var returnValue = new AchievementEntity();
        returnValue.setId(achievement.getId());
        returnValue.setCode(achievement.getCode());
        returnValue.setName(achievement.getName());
        returnValue.setDescription(achievement.getDescription());
        return returnValue;
    }

    public List<AchievementEntity> toEntities(List<Achievement> achievements){
        return achievements.stream().map(this::toEntity).collect(Collectors.toList()); //copied from Mr. Braun's example project.
    }

    //to Domain
    public Achievement toDomain(AchievementEntity achievementEntity){
       final var returnValue = new Achievement(
               achievementEntity.getId(),
               achievementEntity.getCode(),
               achievementEntity.getName(),
               achievementEntity.getDescription()
       );
        return returnValue;
    }
    public List<Achievement> toDomains(List<AchievementEntity> achievementEntities){
        return achievementEntities.stream().map(this::toDomain).collect(Collectors.toList());
    }

}
