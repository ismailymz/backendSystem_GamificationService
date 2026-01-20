package de.thws.gamification.application.ports.in;

import de.thws.gamification.domain.model.DriverProfile;

import java.util.UUID;
import java.util.List;

public interface ViewDriverProfileQuery {

    DriverProfile getProfile(UUID driverId);
    List<DriverProfile> searchDrivers(String username, String role, Integer minScore);

}
