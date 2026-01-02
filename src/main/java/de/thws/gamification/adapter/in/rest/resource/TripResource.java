package de.thws.gamification.adapter.in.rest.resource;

import de.thws.gamification.adapter.in.rest.dto.response.VoidTripResponseDTO;
import de.thws.gamification.application.ports.in.VoidTripUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("/api/trips")
@Produces(MediaType.APPLICATION_JSON)
public class TripResource {

    @Inject
    VoidTripUseCase voidTripUseCase;

    @DELETE
    @Path("/{tripId}")
    public VoidTripResponseDTO voidTrip(@PathParam("tripId") UUID tripId) {
        voidTripUseCase.voidTrip(tripId);

        VoidTripResponseDTO dto = new VoidTripResponseDTO();
        dto.tripId = tripId;
        dto.status = "VOIDED";
        return dto;
    }

    // Eğer contract POST /void diyorsa:
    // @POST
    // @Path("/{tripId}/void")
    // public VoidTripResponseDTO voidTripPost(@PathParam("tripId") UUID tripId) { ... }
}