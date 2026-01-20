package de.thws.gamification.application.service;

import de.thws.gamification.application.ports.in.ViewDriverProfileQuery;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.domain.model.DriverProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
@ApplicationScoped
public class ViewDriverProfileService implements ViewDriverProfileQuery {

    private final DriverProfileRepository driverProfileRepository;
@Inject
    public ViewDriverProfileService(DriverProfileRepository driverProfileRepository) {
        this.driverProfileRepository = driverProfileRepository;
    }
    @Override
    public DriverProfile getProfile(UUID driverId){
        if (driverId==null){
            throw new IllegalArgumentException("driverıd kann nicht null sein");
        }
            return driverProfileRepository.findById(driverId)
                    .orElseThrow(()-> new NoSuchElementException("drivercannot found:"+driverId));


    }

    @Override
    public List<DriverProfile> searchDrivers(String username, String role, Integer minScore) {
        // İş mantığı gerekirse buraya eklenir (örn: loglama)
        return driverProfileRepository.searchDrivers(username, role, minScore);
    }


}
