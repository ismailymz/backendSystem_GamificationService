package de.thws.gamification.application.service;


import de.thws.gamification.application.ports.in.RetrieveTripsQuery;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.TripReport;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RetrieveTripsService implements RetrieveTripsQuery {

    @Inject
    TripReportRepository tripRepository;

    @Override
    public List<TripReport> getTripsByDriverId(UUID driverId) {

        return tripRepository.findByDriverId(driverId);
    }
}