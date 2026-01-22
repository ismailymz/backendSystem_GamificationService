package gamification.adapter.out.persistence.mapper;

import de.thws.gamification.adapter.out.persistence.entity.AchievementEntity;
import de.thws.gamification.adapter.out.persistence.entity.DriverAchievementEntity;
import de.thws.gamification.adapter.out.persistence.entity.DriverProfileEntity;
import de.thws.gamification.adapter.out.persistence.entity.TripReportEntity;
import de.thws.gamification.adapter.out.persistence.mapper.AchievementMapper;
import de.thws.gamification.adapter.out.persistence.mapper.DriverAchievementMapper;
import de.thws.gamification.adapter.out.persistence.mapper.DriverProfileMapper;
import de.thws.gamification.adapter.out.persistence.mapper.TripReportMapper;
import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverAchievement;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static de.thws.gamification.domain.model.TripReport.newReport;
import static org.junit.jupiter.api.Assertions.assertEquals;
@QuarkusTest
public class MapperTest {

    @Inject
    AchievementMapper achievementMapper;
    @Inject
    DriverProfileMapper driverProfileMapper;
    @Inject
    TripReportMapper tripReportMapper;
    @Inject
    DriverAchievementMapper driverAchievementMapper;

    //toEntity Tests


    @Test
    void achievementDomainToEntity(){
        Achievement ach = Achievement.of("NIGHT_DRIVER","NIGHT_DRIVER", "You drive often at nights!");
        AchievementEntity entity = achievementMapper.toEntity(ach);
        assertEquals(entity.getId(), ach.getId());
        assertEquals( entity.getCode(), ach.getCode());
        assertEquals( entity.getName(), ach.getName());
        assertEquals( entity.getDescription(), ach.getDescription());
    }
    @Test
    void achievementDomainsToEntities(){
        //List<Achievement> to List<AchievementEntity>
        Achievement ach = Achievement.of("NIGHT_DRIVER","NIGHT_DRIVER", "You drive often at nights!");
        Achievement ach1 = Achievement.of("SAFE_DRIVER","SAFE_DRIVER", "You drive often at nights!");

        List<Achievement> domainList = new ArrayList<>();
        domainList.add(ach);
        domainList.add(ach1);

        List<AchievementEntity> entityList = achievementMapper.toEntities(domainList);
        for(int i = 0; i<entityList.size(); i++){
            assertEquals(entityList.get(i).getId(), domainList.get(i).getId());
            assertEquals(entityList.get(i).getCode(), domainList.get(i).getCode());
            assertEquals(entityList.get(i).getName(), domainList.get(i).getName());
            assertEquals(entityList.get(i).getDescription(), domainList.get(i).getDescription());
        }

    }
    @Test
    void driverProfileDomainToEntity(){
        DriverProfile profile = DriverProfile.createProfile("Erdil", "erdilschreibt8Klausuren");
        DriverProfileEntity entity = driverProfileMapper.toEntity(profile);
        assertEquals(entity.getId(), profile.getId());
        assertEquals(entity.getUsername(), profile.getUsername());
        assertEquals(entity.getPassword(), profile.getPassword());
    }
    @Test
    void driverProfileDomainsToEntities(){
        DriverProfile profile = DriverProfile.createProfile("Erdil", "erdilschreibt8Klausuren");
        DriverProfile profile1 = DriverProfile.createProfile("Lewandowski", "kommZuFenerbahce!");
        DriverProfile profile2 = DriverProfile.createProfile("N'Golo Kante", "Kann ich auch kommen?");

        List<DriverProfile> domainList = new ArrayList<>();
        domainList.add(profile);
        domainList.add(profile1);
        domainList.add(profile2);
        List<DriverProfileEntity> entityList = driverProfileMapper.toEntities(domainList);

        for(int i = 0; i<entityList.size(); i++){
            assertEquals(entityList.get(i).getId(), domainList.get(i).getId());
            assertEquals(entityList.get(i).getUsername(), domainList.get(i).getUsername());
            assertEquals(entityList.get(i).getPassword(), domainList.get(i).getPassword());
        }
    }


    @Test
    void tripReportDomainToEntity(){
        DriverProfile driver = DriverProfile.createProfile("Erdil", "erdilschreibt8Klausuren");
        TripReport report = newReport(driver.getId(),
                120,
                3,
                4,
                true,
                LocalDateTime.of(2026, 1, 14, 16, 45),
                LocalDateTime.of(2026, 1, 14, 18, 52) );

       TripReportEntity entity =  tripReportMapper.toEntity(report, driverProfileMapper.toEntity(driver));
        assertEquals(entity.getDistanceKm(), report.getDistanceKm());
        assertEquals(entity.getBrakeCount(), report.getBrakeCount());
        assertEquals(entity.getHardAccelerationCount(),report.getHardAccelerationCount());
        assertEquals(entity.isNightTrip(),report.isNightTrip());
        assertEquals(entity.getStartedAt(), report.getStartedAt());
        assertEquals(entity.getEndedAt(),report.getEndedAt());
    }

    @Test
    void tripReportDomainsToEntities(){
        DriverProfile profile = DriverProfile.createProfile("Erdil", "erdilschreibt8Klausuren");
        TripReport report = newReport(profile.getId(),120, 3, 4, true, LocalDateTime.of(2026, 1, 14, 16, 45), LocalDateTime.of(2026, 1, 14, 18, 52) );
        TripReport report1 = newReport(profile.getId(),31, 3, 4, false, LocalDateTime.of(2024, 1, 14, 16, 45), LocalDateTime.of(2024, 1, 14, 18, 52) );
        TripReport report2 = newReport(profile.getId(),231, 3, 4, true, LocalDateTime.of(2026, 1, 14, 16, 45), LocalDateTime.of(2026, 1, 14, 18, 52) );
        List<TripReport> domainList = new ArrayList<>();
        domainList.add(report);
        domainList.add(report1);
        domainList.add(report2);
        List<TripReportEntity> entityList =  tripReportMapper.toEntity(domainList, driverProfileMapper.toEntity(profile));

        for(int i = 0; i < entityList.size(); i++){
            assertEquals(domainList.get(i).getDistanceKm(), entityList.get(i).getDistanceKm());
            assertEquals(domainList.get(i).getBrakeCount(), entityList.get(i).getBrakeCount());
            assertEquals(domainList.get(i).getHardAccelerationCount(),entityList.get(i).getHardAccelerationCount());
            assertEquals(domainList.get(i).isNightTrip(),entityList.get(i).isNightTrip());
            assertEquals(domainList.get(i).getStartedAt(), entityList.get(i).getStartedAt());
            assertEquals(domainList.get(i).getEndedAt(),entityList.get(i).getEndedAt());
        }
    }
    @Test
    void driverAchievementDomainToEntity(){
        DriverProfile profile = DriverProfile.createProfile("Erdil", "erdilschreibt8Klausuren");
        Achievement ach = Achievement.of("NIGHT_DRIVER","Dark kNight", "You drive often at nights!");
        DriverAchievement domain =  DriverAchievement.of(profile.getId(), ach, LocalDateTime.of(2026, 1,21,14,25));

        DriverAchievementEntity entity = driverAchievementMapper.toEntity(domain, driverProfileMapper.toEntity(profile), achievementMapper.toEntity(ach));
       assertEquals(profile.getId(),entity.getDriver().getId());
    }

    //to Domain Tests

    @Test
    void achievementEntityToDomain(){
        AchievementEntity entity = new AchievementEntity(); //id code name description
        entity.setId(UUID.randomUUID());
        entity.setCode("NIGHT_DRIVE");
        entity.setName("All Night Fortnite!");
        entity.setDescription("Hör auf zu spielen und lern!");

        Achievement ach = achievementMapper.toDomain(entity);
        assertEquals(entity.getId(), ach.getId());
        assertEquals(entity.getCode(), ach.getCode());
        assertEquals(entity.getName(), ach.getName());
        assertEquals(entity.getDescription(), ach.getDescription());
    }
    @Test
    void driverProfileEntityToDomain(){
        DriverProfileEntity entity = new DriverProfileEntity();
        entity.setId(UUID.randomUUID());
        entity.setUsername("Erdil Babaaaaa");
        entity.setRole("Fussball Spieler");
        entity.setPassword("BackendQuizWasReallyHard!");
        DriverProfile domain = driverProfileMapper.toDomain(entity);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getUsername(), domain.getUsername());
        assertEquals(entity.getRole(), domain.getRole());
        assertEquals(entity.getPassword(), domain.getPassword());

    }
    @Test
    void tripReportEntityToDomain(){
        DriverProfileEntity driverEntity = new DriverProfileEntity();
        driverEntity.setId(UUID.randomUUID());
        driverEntity.setUsername("Arnold Schwarzenegger");
        driverEntity.setRole("Regular Guy");
        driverEntity.setPassword("IdontuseRESTAPI");
        TripReportEntity entity = new TripReportEntity();
        entity.setId(UUID.randomUUID());
        entity.setDriver(driverEntity);
        entity.setDistanceKm(120);
        entity.setBrakeCount(3);
        entity.setHardAccelerationCount(4);
        entity.setStartedAt(LocalDateTime.of(2026, 1, 14, 16, 45));
        entity.setEndedAt(LocalDateTime.of(2026, 1, 14, 18, 52) );
        TripReport domain = tripReportMapper.toDomain(entity);
        assertEquals(entity.getDriver().getId(), domain.getDriverId());
        assertEquals(entity.getDistanceKm(), domain.getDistanceKm());
        assertEquals(entity.getBrakeCount(), domain.getBrakeCount());
        assertEquals(entity.getHardAccelerationCount(),domain.getHardAccelerationCount());
        assertEquals(entity.isNightTrip(),domain.isNightTrip());
        assertEquals(entity.getStartedAt(), domain.getStartedAt());
        assertEquals(entity.getEndedAt(),domain.getEndedAt());


    }
    @Test
    void DriverAchievementEntityToDomain(){

        DriverProfileEntity profileEntity = new DriverProfileEntity();
        profileEntity.setId(UUID.randomUUID());
        profileEntity.setUsername("thwsVictim");
        profileEntity.setPassword("ilostmymindwährendderklausurenphase");
        AchievementEntity achEntity = new AchievementEntity();
        achEntity.setId(UUID.randomUUID());
        achEntity.setCode("SAFE_DRIVER");
        achEntity.setName("Covered");
        achEntity.setDescription("Don't you have a gas pedal? :D");
        DriverAchievementEntity entity = new DriverAchievementEntity();
        entity.setEarnedAt(LocalDateTime.of(2002,12,23,9,30));
        entity.setId(UUID.randomUUID());
        entity.setDriver(profileEntity);
        entity.setAchievement(achEntity);
        DriverAchievement domain = driverAchievementMapper.toDomain(entity);
        assertEquals(entity.getDriver().getId(),domain.getDriverId());
        assertEquals(entity.getAchievement().getId(), domain.getAchievement().getId());
        assertEquals(entity.getAchievement().getCode(), domain.getAchievement().getCode());
        assertEquals(entity.getAchievement().getName(), domain.getAchievement().getName());
        assertEquals(entity.getAchievement().getDescription(), domain.getAchievement().getDescription());
        assertEquals(entity.getDriver().getId(),domain.getDriverId());
        // ?
    }


}

