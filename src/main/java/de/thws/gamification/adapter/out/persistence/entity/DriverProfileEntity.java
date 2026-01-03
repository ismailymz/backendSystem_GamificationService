package de.thws.gamification.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class DriverProfileEntity {

    @Id
    private UUID id;

    private String username;
    private int totalPoints;

}