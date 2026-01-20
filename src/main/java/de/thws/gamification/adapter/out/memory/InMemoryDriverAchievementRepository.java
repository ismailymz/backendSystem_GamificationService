package de.thws.gamification.adapter.out.memory;

import de.thws.gamification.application.ports.out.DriverAchievementRepository;
import de.thws.gamification.domain.model.DriverAchievement;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
//we dont need this class i deleted others
public class InMemoryDriverAchievementRepository implements DriverAchievementRepository {


    private final List<DriverAchievement> store = new CopyOnWriteArrayList<>();

    @Override
    public List<DriverAchievement> findByDriverId(UUID driverId) {
        return store.stream()
                .filter(da -> driverId.equals(da.getDriverId()))
                .collect(Collectors.toList());
    }


    @Override
    public void deleteByDriverId(UUID driverId) {
        // Listeden, ID'si eşleşenleri çıkarır
        store.removeIf(da -> driverId.equals(da.getDriverId()));
    }

    @Override
    public void saveAll(List<DriverAchievement> achievements) {
        if (achievements == null || achievements.isEmpty()) return;
        store.addAll(achievements);
    }
}