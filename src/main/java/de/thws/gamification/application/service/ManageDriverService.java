package de.thws.gamification.application.service;

import de.thws.gamification.application.ports.in.ManageDriverUseCase;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.domain.model.DriverProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;

@ApplicationScoped
public class ManageDriverService implements ManageDriverUseCase {

    @Inject
    DriverProfileRepository driverRepository;

    @Override
    @Transactional
    public DriverProfile createDriver(String username, String password) {

        DriverProfile newDriver = DriverProfile.createProfile(username, password);

        driverRepository.save(newDriver);
        return newDriver;
    }

    @Override
    @Transactional
    public void deleteDriver(UUID driverId) {
        boolean deleted = driverRepository.deleteById(driverId);
        if (!deleted) {
            throw new NotFoundException("Driver not found with ID: " + driverId);
        }
    }

    @Override
    @Transactional
    public DriverProfile updateDriver(UUID driverId, String newUsername) {
        DriverProfile driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new NotFoundException("Driver not found"));

        driver.changeUsername(newUsername);

        driverRepository.save(driver);
        return driver;
    }
}