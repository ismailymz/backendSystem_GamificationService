package de.thws.gamification.adapter.in.rest.resource;

import de.thws.gamification.adapter.in.rest.dto.request.CreateDriverRequestDTO;
import de.thws.gamification.adapter.in.rest.dto.response.DriverProfileResponseDTO;
import de.thws.gamification.adapter.in.rest.mapper.DriverWebMapper;
import de.thws.gamification.application.ports.in.ManageDriverUseCase;
import de.thws.gamification.application.ports.in.ViewDriverProfileQuery;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/api/drivers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DriverResource {

    @Inject
    ViewDriverProfileQuery viewDriverProfileQuery;

    @Inject
    ManageDriverUseCase manageDriverUseCase;

    @Inject
    DriverWebMapper driverMapper;

    @Context
    UriInfo uriInfo;

    //"The API must implement filtering"
    // GET /api/drivers?username=ism&role=DRIVER&minScore=50 could be good example acording to seeder
    @GET
    public Response getAllDrivers(
            @QueryParam("username") String username,
            @QueryParam("role") String role,
            @QueryParam("minScore") Integer minScore,
            @Context SecurityContext securityContext) {

        //Only admin can access all drivers data
        if (!securityContext.isUserInRole("ADMIN")) {
             return Response.status(Response.Status.FORBIDDEN).build();
        }


        var drivers = viewDriverProfileQuery.searchDrivers(username, role, minScore);

        //adding links
        List<DriverProfileResponseDTO> responseDTOs = drivers.stream()
                .map(driver -> {
                    DriverProfileResponseDTO dto = driverMapper.toDTO(driver);
                    addDriverLinks(dto, driver.getId());
                    return dto;
                })
                .collect(Collectors.toList());

        return Response.ok(responseDTOs).build();
    }

    // (REGISTER) process ---
    @POST
    public Response createDriver(@Valid CreateDriverRequestDTO request) {
        var createdDriver = manageDriverUseCase.createDriver(request.username, request.password);
        DriverProfileResponseDTO responseDTO = driverMapper.toDTO(createdDriver);
        addDriverLinks(responseDTO, createdDriver.getId());
        String selfUrl = getDriverUrl(createdDriver.getId());
        return Response.created(URI.create(selfUrl)) //status should 201 (Note for testing)
                .entity(responseDTO)
                .build();
    }

    @DELETE
    @Path("/{driverId}")
    public Response deleteDriver(@PathParam("driverId") UUID driverId,
                                 @Context SecurityContext securityContext) {

        // MANUEL security
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"message\": \"Access Denied: Only ADMIN can delete drivers.\"}")
                    .build();
        }

        manageDriverUseCase.deleteDriver(driverId);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{driverId}")
    public Response updateDriver(@PathParam("driverId") UUID driverId,
                                 @Valid CreateDriverRequestDTO request,
                                 @Context SecurityContext securityContext) {

        // still MANUEL security
        boolean isDriver = securityContext.isUserInRole("DRIVER");
        boolean isAdmin = securityContext.isUserInRole("ADMIN");

        var driver = viewDriverProfileQuery.getProfile(driverId);
        String loggedInUsername = securityContext.getUserPrincipal().getName();
        if  ( !(loggedInUsername.equals(driver.getUsername())) && isDriver && !isAdmin) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        var updatedDriver = manageDriverUseCase.updateDriver(driverId, request.username);
        DriverProfileResponseDTO responseDTO = driverMapper.toDTO(updatedDriver);
        addDriverLinks(responseDTO, driverId);
        return Response.ok(responseDTO).build();
    }


    @GET
    @Path("/{driverId}/profile")
    public Response getProfile(@PathParam("driverId") UUID driverId,
                               @Context SecurityContext securityContext) {

        // MANUEL GÜVENLİK
        boolean isDriver = securityContext.isUserInRole("DRIVER");
        boolean isAdmin = securityContext.isUserInRole("ADMIN");



        var driver = viewDriverProfileQuery.getProfile(driverId);

        //checking if usernames match when role is DRIVER
        String loggedInUsername = securityContext.getUserPrincipal().getName();
        if ( !(loggedInUsername.equals(driver.getUsername())) && isDriver && !isAdmin) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        DriverProfileResponseDTO responseDTO = driverMapper.toDTO(driver);
        addDriverLinks(responseDTO, driverId);

        return Response.ok(responseDTO).build();
    }

   //methods for  dry principe
    private void addDriverLinks(DriverProfileResponseDTO dto, UUID driverId) {
        // HATEOAS: Self link
        dto.addLink("self", getDriverUrl(driverId));
        // HATEOAS: Trips link (Sürücünün sürüşleri)
        dto.addLink("trips", getTripsUrl(driverId));
    }

    private String getDriverUrl(UUID driverId) {
        return uriInfo.getBaseUriBuilder()
                .path(DriverResource.class)
                .path(driverId.toString() + "/profile")
                .build()
                .toString();
    }

    private String getTripsUrl(UUID driverId) {
        return uriInfo.getBaseUriBuilder()
                .path(TripResource.class)
                .path("driver/" + driverId.toString())
                .build()
                .toString();
    }
}