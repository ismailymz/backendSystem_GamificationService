package de.thws.gamification.config;

import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.domain.model.DriverProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class DevDataSeeder {

    private static final Logger LOG = Logger.getLogger(DevDataSeeder.class);

    @Inject
    DriverProfileRepository driverProfileRepository;

    void onStart(@Observes StartupEvent ev) {

        // 2 demo driver
        DriverProfile ismail = DriverProfile.newDriver("ismail");
        DriverProfile erkin  = DriverProfile.newDriver("erkin");

        // leaderboard’da fark görünsün diye erkin’e puan ekleyelim
        erkin.addPoints(200);

        driverProfileRepository.save(ismail);
        driverProfileRepository.save(erkin);

        LOG.info("=== DEV SEED DONE ===");
        LOG.infof("ISMAIL driverId = %s", ismail.getId());
        LOG.infof("ERKIN  driverId = %s", erkin.getId());
        LOG.info("=====================");
    }
}