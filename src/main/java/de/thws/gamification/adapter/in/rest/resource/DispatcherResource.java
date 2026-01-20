package de.thws.gamification.adapter.in.rest.resource;

import de.thws.gamification.adapter.in.rest.dto.response.LinkDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.ArrayList;
import java.util.List;

// (Single Entry Point)
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class DispatcherResource {

    @Context
    UriInfo uriInfo;

    @GET
    // @PermitAll <-- doesnt need
    public Response getDispatcher() {
        List<LinkDTO> links = new ArrayList<>();

        //(Create Driver)
        String createDriverUrl = uriInfo.getBaseUriBuilder()
                .path(DriverResource.class) // /api/drivers
                .build()
                .toString();

        links.add(new LinkDTO("create-driver", createDriverUrl));

        //Leaderboard
        String leaderboardUrl = uriInfo.getBaseUriBuilder()
                .path(LeaderboardResource.class)
                .build()
                .toString();
        links.add(new LinkDTO("get-leaderboard", leaderboardUrl));

        // response
        return Response.ok()
                .entity(links)
                .build();
    }
}