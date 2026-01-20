package de.thws.gamification.application.ports.in;

import de.thws.gamification.domain.model.DriverProfile;
import java.util.UUID;

public interface ManageDriverUseCase {
    // GÜNCELLEME Artık password parametresi de almalı
    DriverProfile createDriver(String username, String password);

    void deleteDriver(UUID driverId);

    DriverProfile updateDriver(UUID driverId, String newUsername);
}