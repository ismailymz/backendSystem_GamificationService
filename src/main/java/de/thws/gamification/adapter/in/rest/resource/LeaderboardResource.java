package de.thws.gamification.adapter.in.rest.resource;

import de.thws.gamification.adapter.in.rest.dto.response.LeaderboardResponseDTO;
import de.thws.gamification.adapter.in.rest.mapper.LeaderboardWebMapper;
import de.thws.gamification.application.ports.in.RetrieveLeaderboardQuery;
import de.thws.gamification.domain.model.Period;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;


@Path("/api/leaderboard")
@Produces(MediaType.APPLICATION_JSON)
public class LeaderboardResource {

    @Inject
    RetrieveLeaderboardQuery retrieveLeaderboardQuery;

    @GET
    public LeaderboardResponseDTO getLeaderboard(@QueryParam("period") Period period) {
        if (period == null) {
            throw new BadRequestException("period query param is required (DAILY|WEEKLY|ALL_TIME)");
        }
        var list = retrieveLeaderboardQuery.getLeaderboard(period);
        return LeaderboardWebMapper.toResponse(list);
    }
}