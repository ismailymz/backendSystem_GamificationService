package de.thws.gamification.adapter.in.rest.dto.response;


import java.util.ArrayList;
import java.util.List;

// extends BaseResponse for links
public class TripListResponseDTO extends BaseResponseDTO {

    public List<TripResponseDTO> trips = new ArrayList<>();
    public int size;

    // Boş constructor (Jackson için gerekli)
    public TripListResponseDTO() {}
}