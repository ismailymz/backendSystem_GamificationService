package de.thws.gamification.application.service;

import de.thws.gamification.application.ports.in.VoidTripUseCase;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.TripReport;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.NoSuchElementException;
import java.util.UUID;
@ApplicationScoped
public class VoidTripService implements VoidTripUseCase {

    private final TripReportRepository tripReportRepository;

   @Inject
    public VoidTripService(TripReportRepository tripReportRepository) {
        this.tripReportRepository = tripReportRepository;
    }

    public void voidTrip(UUID tripId){
        if (tripId==null){
            throw new IllegalArgumentException("trip id 0 olamaz");
        }
        TripReport report = tripReportRepository.findById(tripId)
                .orElseThrow(()->new NoSuchElementException("trip bulunamadı:"+tripId));
        // 2) Zaten void ise bir şey yapmaya gerek yok (idempotent davranış)

        if (report.isVoided()){
            return;
        }

        report.markVoided();

        tripReportRepository.save(report);
    }

}
