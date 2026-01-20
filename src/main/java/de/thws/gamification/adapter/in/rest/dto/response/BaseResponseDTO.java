package de.thws.gamification.adapter.in.rest.dto.response;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseResponseDTO {
    // i made it abstract i thought other responses can extend from it
    //all my links in a List here
    public List<LinkDTO> links = new ArrayList<>();

    //add method
    public void addLink(String rel, String href) {
        this.links.add(new LinkDTO(rel, href));
    }
}