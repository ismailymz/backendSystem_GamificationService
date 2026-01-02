package de.thws.gamification.adapter.in.rest.resource;

import de.thws.gamification.adapter.in.rest.dto.request.SubmitTripRequestDTO;
import de.thws.gamification.adapter.in.rest.dto.response.DriverProfileResponseDTO;
import de.thws.gamification.adapter.in.rest.dto.response.SubmitTripResponseDTO;
import de.thws.gamification.adapter.in.rest.mapper.DriverWebMapper;
import de.thws.gamification.adapter.in.rest.mapper.TripWebMapper;
import de.thws.gamification.application.ports.in.SubmitTripReportUseCase;
import de.thws.gamification.application.ports.in.ViewDriverProfileQuery;
import de.thws.gamification.domain.service.GamificationResult;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/api/drivers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DriverResource {

    @Inject
    SubmitTripReportUseCase submitTripReportUseCase;

    @Inject
    ViewDriverProfileQuery viewDriverProfileQuery;

    @POST
    @Path("/{driverId}/trips")
    public Response submitTrip(@PathParam("driverId") UUID driverId,
                               @Valid SubmitTripRequestDTO body) {

        var report = TripWebMapper.toDomain(driverId, body);
        GamificationResult result = submitTripReportUseCase.submitTrip(driverId, report);

        SubmitTripResponseDTO response = TripWebMapper.toResponse(driverId, result);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{driverId}/profile")
    public DriverProfileResponseDTO getProfile(@PathParam("driverId") UUID driverId) {
        var driver = viewDriverProfileQuery.getProfile(driverId);
        return DriverWebMapper.toResponse(driver);
    }
}