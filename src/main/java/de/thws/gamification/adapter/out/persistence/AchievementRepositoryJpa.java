package de.thws.gamification.adapter.out.persistence;

import de.thws.gamification.adapter.out.persistence.entity.AchievementEntity;
import de.thws.gamification.adapter.out.persistence.mapper.AchievementMapper;
import de.thws.gamification.adapter.out.persistence.mapper.DriverAchievementMapper;
import de.thws.gamification.application.ports.out.AchievementRepository;
import de.thws.gamification.domain.model.Achievement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.spi.PersistenceProvider;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
@ApplicationScoped
public class AchievementRepositoryJpa implements AchievementRepository {
    @Inject
    EntityManager em;
    @Inject
    AchievementMapper mapper;


    @Override
    public List<Achievement> findAll(){
        final var achievementsFound=em.createQuery
                ("SELECT a FROM AchievementEntity a",AchievementEntity.class).getResultList();
        return mapper.toDomains(achievementsFound);
    }


    @Override
    @Transactional // Yazma işlemi olduğu için Transactional şart
    public void save(Achievement achievement) {

        AchievementEntity entity = new AchievementEntity();
        entity.setId(achievement.getId());
        entity.setCode(achievement.getCode());
        entity.setName(achievement.getName());
        entity.setDescription(achievement.getDescription());

        //merge (!)
        em.merge(entity);
    }


    @Override
    public Optional<Achievement> findByCode(String code) {
        try {
            final var achievementFound = em.createQuery(
                            "SELECT a FROM AchievementEntity a WHERE a.code = :code",
                            AchievementEntity.class)
                    .setParameter("code", code)
                    .getSingleResult();

            return Optional.of(mapper.toDomain(achievementFound));

        } catch (jakarta.persistence.NoResultException e) {
            // Kayıt yoksa hata fırlatma, boş kutu (Optional) dön
            return Optional.empty();
        }
    }
}
