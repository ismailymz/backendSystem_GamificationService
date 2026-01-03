package de.thws.gamification.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;


public class TripReportEntity {

    @Id
    private UUID id;

    //FK

    private int distanceKm;
    private int brakeCount;
    private int hardAccelerationCount;
    private boolean nightTrip;

}