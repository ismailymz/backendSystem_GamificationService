package de.thws.gamification.application.ports.out;

import de.thws.gamification.domain.model.DriverAchievement;

import java.util.List;
import java.util.UUID;

public interface DriverAchievementRepository {
    List<DriverAchievement> findByDriverId(UUID driverId);
    void saveAll(List<DriverAchievement> achievements);
    void deleteByDriverId(UUID driverId);
}
