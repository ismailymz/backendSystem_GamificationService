package de.thws.gamification.adapter.out.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "achievements")
public class AchievementEntity {
    @Id
    private UUID id;

    @OneToMany(mappedBy = "achievement")
    private List<DriverAchievementEntity> earnedAchievements = new ArrayList<>();

    private String code;
    private String name;
    private String description;

    public AchievementEntity() {}

    //getter
    public UUID getId() {
        return id;
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    //setter
    public void setId(UUID id) {
        this.id = id;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
