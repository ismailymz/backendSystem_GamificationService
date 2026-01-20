package de.thws.gamification.application.ports.out;

import de.thws.gamification.domain.model.Achievement;

import java.util.List;
import java.util.Optional;

public interface AchievementRepository {

    List<Achievement> findAll();
    Optional<Achievement> findByCode(String code);
    void save(Achievement achievement);
}
