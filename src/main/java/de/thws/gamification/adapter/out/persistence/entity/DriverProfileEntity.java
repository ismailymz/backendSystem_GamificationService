package de.thws.gamification.adapter.out.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;


@Entity
public class DriverProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String username;




}
