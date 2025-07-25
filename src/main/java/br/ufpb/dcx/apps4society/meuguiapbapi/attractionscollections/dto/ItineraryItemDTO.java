package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain.ItineraryItem;

public class ItineraryItemDTO {
    private Long attractionId;
    private Integer position;
    private Integer rating;

    public ItineraryItemDTO() {
    }

    public ItineraryItemDTO(Long attractionId, Integer position, Integer rating) {
        this.attractionId = attractionId;
        this.position = position;
        this.rating = rating;
    }

    public ItineraryItemDTO(ItineraryItem itineraryItem) {
        this(itineraryItem.getAttraction().getId(), itineraryItem.getPosition(), itineraryItem.getRating());
    }

    public Long getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(Long attractionId) {
        this.attractionId = attractionId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ItineraryItemDTO{" +
                "attractionId=" + attractionId +
                ", position=" + position +
                ", rating=" + rating +
                '}';
    }
}
