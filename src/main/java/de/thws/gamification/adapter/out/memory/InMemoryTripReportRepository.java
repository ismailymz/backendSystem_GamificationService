package de.thws.gamification.adapter.out.memory;

import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.TripReport;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryTripReportRepository implements TripReportRepository {

    private final Map<UUID, TripReport> store = new ConcurrentHashMap<>();

    @Override
    public void save(TripReport report) {
        store.put(report.getId(), report);
    }

    @Override
    public Optional<TripReport> findById(UUID tripId) {
        return Optional.ofNullable(store.get(tripId));
    }

    @Override
    public List<TripReport> findByDriverId(UUID driverId) {
        return store.values().stream()
                .filter(r -> driverId.equals(r.getDriverId()))
                .collect(Collectors.toList());
    }
}