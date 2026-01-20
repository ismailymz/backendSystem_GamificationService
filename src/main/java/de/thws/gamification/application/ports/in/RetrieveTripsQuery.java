package de.thws.gamification.application.ports.in;

import de.thws.gamification.domain.model.TripReport;

import java.util.List;
import java.util.UUID;

public interface RetrieveTripsQuery {
    List<TripReport> getTripsByDriverId(UUID driverId);
}
