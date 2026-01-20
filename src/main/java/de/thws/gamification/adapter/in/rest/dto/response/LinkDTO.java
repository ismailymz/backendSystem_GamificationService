package de.thws.gamification.adapter.in.rest.dto.response;


public class LinkDTO {

    public String rel;
    public String href;

    public LinkDTO() {}

    public LinkDTO(String rel, String href) {
        this.rel = rel;
        this.href = href;
    }
}

