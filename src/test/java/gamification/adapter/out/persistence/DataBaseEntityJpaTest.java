package gamification.adapter.out.persistence;

import de.thws.gamification.adapter.out.persistence.entity.*;
import de.thws.gamification.adapter.out.persistence.mapper.*;
import de.thws.gamification.domain.model.*;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class DataBaseEntityJpaTest {

    @Inject EntityManager em;

    @Inject AchievementMapper achievementMapper;
    @Inject DriverProfileMapper driverProfileMapper;
    @Inject TripReportMapper tripReportMapper;
    @Inject DriverAchievementMapper driverAchievementMapper;

    private static void step(String msg) { System.out.println("\n========== " + msg + " =========="); }
    private static void ok(String msg)   { System.out.println("✅ " + msg); }
    private static void info(String msg) { System.out.println("ℹ️  " + msg); }

    @Test
    @Transactional
    void shouldCreateTwoDomainObjectsPerEntity_thenMapAndPersist() {
        step("0) Achievements count = " +
                em.createQuery("select count(a) from AchievementEntity a", Long.class).getSingleResult());
        step("1) DOMAIN oluşturma");

        /* -------------------- DOMAIN: 2 DriverProfile -------------------- */
        DriverProfile d1 = DriverProfile.createProfile("driver_one", "123abc");
        DriverProfile d2 = DriverProfile.createProfile("driver_two", "abc123");
        info("DriverProfile domain oluşturuldu: d1=" + d1);
        info("DriverProfile domain oluşturuldu: d2=" + d2);

        /* -------------------- DOMAIN: 2 Achievement -------------------- */
        Achievement a1 = Achievement.of("FIRST_TRIP", "First Trip", "Completed first trip");
        Achievement a2 = Achievement.of("NIGHT_DRIVER", "Night Driver", "Drove at night");
        info("Achievement domain oluşturuldu: a1=" + a1);
        info("Achievement domain oluşturuldu: a2=" + a2);

        step("2) DOMAIN -> ENTITY map (DriverProfile, Achievement)");

        DriverProfileEntity d1e = driverProfileMapper.toEntity(d1);
        DriverProfileEntity d2e = driverProfileMapper.toEntity(d2);
        info("DriverProfileEntity map edildi: d1e=" + d1e);
        info("DriverProfileEntity map edildi: d2e=" + d2e);

        AchievementEntity a1e = achievementMapper.toEntity(a1);
        AchievementEntity a2e = achievementMapper.toEntity(a2);
        info("AchievementEntity map edildi: a1e=" + a1e);
        info("AchievementEntity map edildi: a2e=" + a2e);

        step("3) PERSIST (DriverProfileEntity, AchievementEntity)");

        em.persist(d1e); ok("Persist edildi: DriverProfileEntity d1e (id=" + d1e.getId() + ")");
        em.persist(d2e); ok("Persist edildi: DriverProfileEntity d2e (id=" + d2e.getId() + ")");
        em.persist(a1e); ok("Persist edildi: AchievementEntity a1e (id=" + a1e.getId() + ")");
        em.persist(a2e); ok("Persist edildi: AchievementEntity a2e (id=" + a2e.getId() + ")");

        step("4) DOMAIN oluşturma (TripReport)");

        /* -------------------- DOMAIN: 2 TripReport -------------------- */
        TripReport tr1 = new TripReport(
                UUID.randomUUID(),
                d1.getId(),
                12,
                2,
                1,
                false,
                LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now()
        );

        TripReport tr2 = new TripReport(
                UUID.randomUUID(),
                d2.getId(),
                45,
                0,
                3,
                true,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now()
        );

        info("TripReport domain oluşturuldu: tr1=" + tr1);
        info("TripReport domain oluşturuldu: tr2=" + tr2);

        step("5) DOMAIN -> ENTITY map + PERSIST (TripReportEntity)");

        TripReportEntity tr1e = tripReportMapper.toEntity(tr1, d1e);
        TripReportEntity tr2e = tripReportMapper.toEntity(tr2, d2e);
        info("TripReportEntity map edildi: tr1e=" + tr1e);
        info("TripReportEntity map edildi: tr2e=" + tr2e);

        em.persist(tr1e); ok("Persist edildi: TripReportEntity tr1e (id=" + tr1e.getId() + ")");
        em.persist(tr2e); ok("Persist edildi: TripReportEntity tr2e (id=" + tr2e.getId() + ")");

        step("6) DOMAIN oluşturma (DriverAchievement)");

        /* -------------------- DOMAIN: 2 DriverAchievement -------------------- */
        DriverAchievement da1 = DriverAchievement.of(d1.getId(), a1, LocalDateTime.now());
        DriverAchievement da2 = DriverAchievement.of(d2.getId(), a2, LocalDateTime.now());
        info("DriverAchievement domain oluşturuldu: da1=" + da1);
        info("DriverAchievement domain oluşturuldu: da2=" + da2);

        step("7) DOMAIN -> ENTITY map + PERSIST (DriverAchievementEntity)");

        DriverAchievementEntity da1e = driverAchievementMapper.toEntity(da1, d1e, a1e);
        DriverAchievementEntity da2e = driverAchievementMapper.toEntity(da2, d2e, a2e);
        info("DriverAchievementEntity map edildi: da1e=" + da1e);
        info("DriverAchievementEntity map edildi: da2e=" + da2e);

        em.persist(da1e); ok("Persist edildi: DriverAchievementEntity da1e (id=" + da1e.getId() + ")");
        em.persist(da2e); ok("Persist edildi: DriverAchievementEntity da2e (id=" + da2e.getId() + ")");

        step("8) FLUSH + CLEAR");
        em.flush();
        em.clear();
        ok("flush() ve clear() tamam.");

        step("9) DB'den FIND + assert + ekrana yazdırma");

        DriverProfileEntity d1Found = em.find(DriverProfileEntity.class, d1e.getId());
        DriverProfileEntity d2Found = em.find(DriverProfileEntity.class, d2e.getId());
        assertNotNull(d1Found); ok("Bulundu: DriverProfileEntity -> " + d1Found);
        assertNotNull(d2Found); ok("Bulundu: DriverProfileEntity -> " + d2Found);

        AchievementEntity a1Found = em.find(AchievementEntity.class, a1e.getId());
        AchievementEntity a2Found = em.find(AchievementEntity.class, a2e.getId());
        assertNotNull(a1Found); ok("Bulundu: AchievementEntity -> " + a1Found);
        assertNotNull(a2Found); ok("Bulundu: AchievementEntity -> " + a2Found);

        TripReportEntity tr1Found = em.find(TripReportEntity.class, tr1e.getId());
        TripReportEntity tr2Found = em.find(TripReportEntity.class, tr2e.getId());
        assertNotNull(tr1Found); ok("Bulundu: TripReportEntity -> " + tr1Found);
        assertNotNull(tr2Found); ok("Bulundu: TripReportEntity -> " + tr2Found);

        DriverAchievementEntity da1Found = em.find(DriverAchievementEntity.class, da1e.getId());
        DriverAchievementEntity da2Found = em.find(DriverAchievementEntity.class, da2e.getId());
        assertNotNull(da1Found); ok("Bulundu: DriverAchievementEntity -> " + da1Found);
        assertNotNull(da2Found); ok("Bulundu: DriverAchievementEntity -> " + da2Found);

        step("TEST BİTTİ");
        ok("Hepsi başarıyla kaydedildi ve bulundu ✅");
    }
}