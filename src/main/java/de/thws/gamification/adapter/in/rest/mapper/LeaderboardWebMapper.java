package de.thws.gamification.adapter.in.rest.mapper;

import de.thws.gamification.adapter.in.rest.dto.response.LeaderboardEntryDTO;
import de.thws.gamification.adapter.in.rest.dto.response.LeaderboardResponseDTO;
import de.thws.gamification.domain.model.LeaderboardEntry;

import java.util.List;
import java.util.stream.Collectors;

public class LeaderboardWebMapper {

    public static LeaderboardResponseDTO toResponse(List<LeaderboardEntry> entries) {
        LeaderboardResponseDTO out = new LeaderboardResponseDTO();
        out.entries = entries.stream().map(LeaderboardWebMapper::map).collect(Collectors.toList());
        return out;
    }

    private static LeaderboardEntryDTO map(LeaderboardEntry e) {
        LeaderboardEntryDTO dto = new LeaderboardEntryDTO();
        dto.driverId = e.getDriverId();
        dto.username = e.getUsername();
        dto.points = e.getPoints();
        dto.rank = e.getRank();
        return dto;
    }
}