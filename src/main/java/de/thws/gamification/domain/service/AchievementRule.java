package de.thws.gamification.domain.service;

import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;

import java.util.Optional;

public interface AchievementRule {
    //bu kural trip üzerinden değerlendirip kazanılan achievement varsa onu döndürür optional olarak

    Optional<Achievement> check(TripReport report, DriverProfile driver);


}
