package de.thws.gamification.adapter.out.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
public class DriverAchievementEntity {


    @Id
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_driver_achievement_driver_id")
    )
    private DriverProfileEntity driver;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_driver_achievement_achievement_id")
    )
    private AchievementEntity achievement;


    private LocalDateTime earnedAt;

    public DriverAchievementEntity() {

    }

    //getter
    public UUID getId() {
        return id;
    }
    public DriverProfileEntity getDriver() {
        return driver;
    }
    public AchievementEntity getAchievement() {
        return achievement;
    }
    public LocalDateTime getEarnedAt() {
        return earnedAt;
    }

    //setter
    public void setId(UUID id) {
        this.id = id;
    }
    public void setDriver(DriverProfileEntity driver) {
        this.driver = driver;
    }
    public void setAchievement(AchievementEntity achievement) {
        this.achievement = achievement;
    }
    public void setEarnedAt(LocalDateTime earnedAt) {
        this.earnedAt = earnedAt;
    }


}
