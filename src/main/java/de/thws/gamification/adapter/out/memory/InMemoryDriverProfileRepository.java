package de.thws.gamification.adapter.out.memory;

import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.domain.model.DriverProfile;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class InMemoryDriverProfileRepository implements DriverProfileRepository {

    private final Map<UUID, DriverProfile> store = new ConcurrentHashMap<>();

    @Override
    public Optional<DriverProfile> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<DriverProfile> findAllOrderByTotalPointsDesc() {
        return store.values().stream()
                .sorted(Comparator.comparingInt(DriverProfile::getTotalPoints).reversed())
                .toList();
    }

    @Override
    public DriverProfile save(DriverProfile driverProfile) {
        store.put(driverProfile.getId(), driverProfile);
        return driverProfile;
    }

    // DEV/TEST için seed helper
    public void seed(DriverProfile d) {
        store.put(d.getId(), d);
    }
}