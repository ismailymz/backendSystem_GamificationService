package de.thws.gamification.adapter.in.rest.resource;

import de.thws.gamification.adapter.in.rest.dto.request.SubmitTripRequestDTO;
import de.thws.gamification.adapter.in.rest.dto.response.SubmitTripResponseDTO;
import de.thws.gamification.adapter.in.rest.dto.response.TripListResponseDTO;
import de.thws.gamification.adapter.in.rest.mapper.TripWebMapper;
import de.thws.gamification.adapter.in.rest.util.CursorUtils;
import de.thws.gamification.application.ports.in.SubmitTripReportUseCase;
import de.thws.gamification.application.ports.out.TripReportRepository;
import de.thws.gamification.domain.model.TripReport;
import de.thws.gamification.domain.service.GamificationResult;

// Not: @RolesAllowed importunu kaldırdık çünkü manuel kontrol yapacağız.
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext; // Manuel güvenlik için şart
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/trips")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TripResource {

    @Inject
    SubmitTripReportUseCase submitTripReportUseCase;

    @Inject
    TripReportRepository tripReportRepository;

    @Inject
    TripWebMapper tripMapper;

    @Context
    UriInfo uriInfo;

    @POST
    @Path("/driver/{driverId}")
    public Response submitTrip(@PathParam("driverId") UUID driverId,
                               @Valid SubmitTripRequestDTO body,
                               @Context SecurityContext securityContext) { // <-- EKLENDİ

        if (!securityContext.isUserInRole("DRIVER")) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"message\": \"Access Denied: Only DRIVERS can submit trips.\"}")
                    .build();
        }

        //Domain part
        TripReport report = tripMapper.toDomain(driverId, body);
        GamificationResult result = submitTripReportUseCase.submitTrip(driverId, report);
        SubmitTripResponseDTO response = tripMapper.toDTO(driverId, result);

        //(HATEOAS)
        String tripSelfUrl = uriInfo.getBaseUriBuilder()
                .path(TripResource.class)
                .path(report.getId().toString())
                .build()
                .toString();

        String driverUrl = uriInfo.getBaseUriBuilder()
                .path(DriverResource.class)
                .path(driverId.toString() + "/profile")
                .build()
                .toString();


        response.addLink("self", tripSelfUrl);
        response.addLink("driver", driverUrl);


        return Response.created(URI.create(tripSelfUrl))
                .entity(response)
                .build();
    }


    @GET
    @Path("/driver/{driverId}")
    public Response getTripsCursor(
            @PathParam("driverId") UUID driverId,
            @QueryParam("cursor") String cursor,
            @DefaultValue("10") @QueryParam("size") int size,
            @Context SecurityContext securityContext) { // <-- EKLENDİ


        boolean isDriver = securityContext.isUserInRole("DRIVER");
        boolean isAdmin = securityContext.isUserInRole("ADMIN");

        if (!isDriver && !isAdmin) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"message\": \"Access Denied: You must be ADMIN or DRIVER.\"}")
                    .build();
        }


        var decodedCursor = CursorUtils.decodeCursor(cursor);


        List<TripReport> trips = tripReportRepository.findTripsByCursor(driverId, size, decodedCursor);


        TripListResponseDTO response = new TripListResponseDTO();
        response.trips = tripMapper.toResponseList(trips);
        response.size = size;


        String selfUrl = createCursorUrl(driverId, size, cursor);
        response.addLink("self", selfUrl);

        if (!trips.isEmpty() && trips.size() == size) {
            TripReport lastTrip = trips.get(trips.size() - 1);
            String nextCursor = CursorUtils.createCursor(lastTrip.getStartedAt(), lastTrip.getId());
            String nextUrl = createCursorUrl(driverId, size, nextCursor);
            response.addLink("next", nextUrl);
        }

        return Response.ok(response).build();
    }

    private String createCursorUrl(UUID driverId, int size, String cursor) {
        var builder = uriInfo.getBaseUriBuilder()
                .path(TripResource.class)
                .path("driver/" + driverId)
                .queryParam("size", size);

        if (cursor != null) {
            builder.queryParam("cursor", cursor);
        }
        return builder.build().toString();
    }
}