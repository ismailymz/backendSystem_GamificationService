package de.thws.gamification.application.ports.in;

import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.GamificationResult;

import java.util.UUID;

public interface SubmitTripReportUseCase {




   GamificationResult submitTrip(UUID driverId, TripReport report);
}
