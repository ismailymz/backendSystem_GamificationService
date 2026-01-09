package de.thws.gamification.adapter.out.memory;

import de.thws.gamification.application.ports.out.DriverAchievementRepository;
import de.thws.gamification.domain.model.DriverAchievement;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class InMemoryDriverAchievementRepository implements DriverAchievementRepository {

    private final List<DriverAchievement> store = new CopyOnWriteArrayList<>();

    @Override
    public List<DriverAchievement> findByDriverId(UUID driverId) {
        return store.stream()
                .filter(da -> driverId.equals(da.getDriverId()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<DriverAchievement> achievements) {
        if (achievements == null || achievements.isEmpty()) return;
        store.addAll(achievements);
    }
}