package de.thws.gamification.adapter.out.persistence.entity;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
public class AchievementEntity {
    @Id
    private UUID id;

    //bk private String code;
    private String name;
    private String description;

}
