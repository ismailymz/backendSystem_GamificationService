package de.thws.gamification.domain.service;

import com.sun.source.tree.ReturnTree;
import de.thws.gamification.domain.model.Achievement;
import de.thws.gamification.domain.model.DriverAchievement;
import de.thws.gamification.domain.model.DriverProfile;
import de.thws.gamification.domain.model.TripReport;
import io.smallrye.mutiny.vertx.codegen.lang.NewInstanceWithGenericsMethodCodeWriter;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.Driver;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class GamificationEngine {
    private final List<ScoringPolicy> scoringPolicies;
    private final List<AchievementRule> achievementRules;

    public GamificationEngine(List<ScoringPolicy> scoringPolicies,
                              List<AchievementRule> achievementRules) {
        this.scoringPolicies = scoringPolicies;
        this.achievementRules = achievementRules;
    }

    //ana akış

    public GamificationResult applyTrip(DriverProfile driver, TripReport report) {
        int totalPoints = 0;
        List<DriverAchievement> newEarned = new ArrayList<>();

        //puan hesabı

        for (ScoringPolicy policy : scoringPolicies) {
            int points = policy.calculatePoints(report, driver);

            totalPoints += points;
        }
        driver.addPoints(totalPoints);

        //Achievement kontrolü bu seferde
        for (AchievementRule rule : achievementRules) {
            rule.check(report, driver).ifPresent(achievement -> {
                DriverAchievement earned = DriverAchievement.of(
                        driver.getId(),
                        achievement,
                        LocalDateTime.now()
                );
                driver.addAchievement(earned);
                newEarned.add(earned);
            });
        }

    return new GamificationResult(driver,totalPoints,newEarned);


}}
