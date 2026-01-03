package de.thws.gamification.adapter.out.persistence.entity;

import de.thws.gamification.domain.model.Achievement;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
public class DriverAchievementEntity {

    @Id
    private UUID id;


    // FK private UUID driverId;
    // FK private UUID achievementId;
    private LocalDateTime earnedAt;

}
