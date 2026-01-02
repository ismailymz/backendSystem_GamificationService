package de.thws.gamification.adapter.out.memory;

import de.thws.gamification.application.ports.out.AchievementRepository;
import de.thws.gamification.domain.model.Achievement;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class InMemoryAchievementRepository implements AchievementRepository {

    private final Map<UUID, Achievement> store = new ConcurrentHashMap<>();

    @Override
    public List<Achievement> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Achievement> findByCode(String code) {
        if (code == null) return Optional.empty();
        return store.values().stream()
                .filter(a -> code.equals(a.getCode()))
                .findFirst();
    }

    // DEV/TEST için seed helper
    public void seed(Achievement a) {
        store.put(a.getId(), a);
    }
}