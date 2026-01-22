package gamification.adapter.out.persistence.jpaAdapterTest;

import de.thws.gamification.domain.model.Achievement;

public class AchievementRepositoryJpaTest {
    //findByCode, save, findAll

    void persistAchievement(){
        Achievement ach1 = Achievement.of("NIGHT_DRIVE", "Owl", "Why do you drive at night often?");

    }

}
