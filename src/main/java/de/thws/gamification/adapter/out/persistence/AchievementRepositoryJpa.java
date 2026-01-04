package de.thws.gamification.adapter.out.persistence;

import de.thws.gamification.application.ports.out.AchievementRepository;
import de.thws.gamification.domain.model.Achievement;

import java.util.List;
import java.util.Optional;

public class AchievementRepositoryJpa implements AchievementRepository {

    //Implementation of AchievementRepository using JPA
    //Must be written!!!

    @Override
    public List<Achievement> findAll(){
        return null;
    }


    @Override
    public Optional<Achievement> findByCode(String code){
        return Optional.empty();
    }
}
