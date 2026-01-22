package de.thws.gamification.config;

import de.thws.gamification.application.ports.out.AchievementRepository;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.UUID;
import io.quarkus.arc.profile.UnlessBuildProfile;
@UnlessBuildProfile("test")


@ApplicationScoped
public class DevDataSeeder {

    private static final Logger LOG = Logger.getLogger(DevDataSeeder.class);

    private static final UUID SAFE_DRIVER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID NIGHT_DRIVER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
@Inject
TripReportRepository reportRepo;
    @Inject
    DriverProfileRepository driverProfileRepository;

    @Inject
    AchievementRepository achievementRepository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        LOG.info("DEV DATA SEEDING STARTED");
        seedAchievements();
        seedDrivers();
        LOG.info("DEV DATA SEEDING DONE");
    }

    private void seedAchievements() {
        if (achievementRepository.findByCode("SAFE_DRIVER").isEmpty()) {
            LOG.info("Creating achievement SAFE_DRIVER");
            Achievement safeDriverBadge = new Achievement(
                    SAFE_DRIVER_ID,
                    "SAFE_DRIVER",
                    "Unbreakable", //erdil's idea
                    "Completed 5 trips without hard brakes!"
            );
            achievementRepository.save(safeDriverBadge);
        }

        if (achievementRepository.findByCode("NIGHT_DRIVER").isEmpty()) {
            LOG.info("Creating missing achievement: NIGHT_DRIVER");
            Achievement nightOwl = new Achievement(
                    NIGHT_DRIVER_ID,
                    "NIGHT_DRIVER",
                    "Night Owl",
                    "Completed 3 trips between 22:00 and 05:00"
            );
            achievementRepository.save(nightOwl);
        }
    }

    private void seedDrivers() {

        DriverProfile ismail = DriverProfile.createProfile("ismail", "1234");
        DriverProfile erkin  = DriverProfile.createProfile("erkin", "1234");
        DriverProfile erdil  = DriverProfile.createProfile("erdil", "1234");
        DriverProfile admin = DriverProfile.createAdmin("admin", "adminpass");


        erkin.addPoints(200);
        erdil.addPoints(350);
        ismail.addPoints(100);

        driverProfileRepository.save(ismail);
        driverProfileRepository.save(erkin);
        driverProfileRepository.save(erdil);
        driverProfileRepository.save(admin); // also Admin

        TripReport trip1 = TripReport.newReport(erdil.getId(), 56, 3,6,true, LocalDateTime.of(2026,1,22,9,10), LocalDateTime.of(2026,1,22,9,50));
        reportRepo.save(trip1);
        TripReport trip2 = TripReport.newReport(erkin.getId(), 56, 3,6,true, LocalDateTime.of(2026,1,22,9,10), LocalDateTime.of(2026,1,22,9,50));
        reportRepo.save(trip2);
    }
}