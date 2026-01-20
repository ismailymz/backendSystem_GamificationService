package de.thws.gamification.adapter.in.rest.resource;

import de.thws.gamification.adapter.in.rest.dto.response.VoidTripResponseDTO;
import de.thws.gamification.application.ports.in.VoidTripUseCase;

// @RolesAllowed importunu SİLDİK
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;       // <-- EKLENDİ
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext; // <-- EKLENDİ (Manuel kontrol için)

import java.util.NoSuchElementException;
import java.util.UUID;

@Path("/api/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {

    @Inject
    VoidTripUseCase voidTripUseCase;

    // admin control added
    // @RolesAllowed("ADMIN") doesnt work so i did it manuel
    @POST
    @Path("/trips/{tripId}/void")
    public Response voidTrip(@PathParam("tripId") UUID tripId,
                             @Context SecurityContext securityContext) { // <-- Context Parametresi


        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"message\": \"Access Denied: Only ADMIN can void trips.\"}")
                    .build();
        }

        try {
            voidTripUseCase.voidTrip(tripId);

            VoidTripResponseDTO response = new VoidTripResponseDTO();
            response.tripId = tripId;
            response.status = "VOIDED";

            return Response.ok(response).build();

        } catch (IllegalArgumentException | NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}