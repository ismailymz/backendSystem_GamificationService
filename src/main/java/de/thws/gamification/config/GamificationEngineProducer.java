package de.thws.gamification.config;

import de.thws.gamification.domain.service.AchievementRule;
import de.thws.gamification.domain.service.GamificationEngine;
import de.thws.gamification.domain.service.ScoringPolicy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class GamificationEngineProducer {

    @Inject
    Instance<ScoringPolicy> scoringPolicies;

    @Inject
    Instance<AchievementRule> achievementRules;

    @Produces
    @ApplicationScoped
    public GamificationEngine gamificationEngine() {
        List<ScoringPolicy> policies = scoringPolicies.stream().toList();
        List<AchievementRule> rules = achievementRules.stream().toList();
        return new GamificationEngine(policies, rules);
    }
}