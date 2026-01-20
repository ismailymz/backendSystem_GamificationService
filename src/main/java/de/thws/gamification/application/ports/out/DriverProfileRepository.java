package de.thws.gamification.application.ports.out;
import de.thws.gamification.domain.model.DriverProfile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface DriverProfileRepository {
    Optional <DriverProfile> findById(UUID id);

    List<DriverProfile> findAllOrderByTotalPointsDesc();
    Optional<DriverProfile> findByUsername(String username);
    DriverProfile save(DriverProfile driverProfile);
    boolean deleteById(UUID id);
    List<DriverProfile> searchDrivers(String usernameFilter, String roleFilter, Integer minScore);
}