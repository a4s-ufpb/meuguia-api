package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import jakarta.persistence.Embeddable;

@Embeddable
public class ItineraryItemId {
    private Long attractionId;
    private Long itineraryId;

    public ItineraryItemId() {
    }

    public ItineraryItemId(Attraction attraction, Itinerary itinerary) {
        this.attractionId = attraction.getId();
        this.itineraryId = itinerary.getId();
    }

    public Long getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(Long attractionId) {
        this.attractionId = attractionId;
    }

    public Long getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }

    @Override
    public int hashCode() {
        int result = attractionId.hashCode();
        result = 31 * result + itineraryId.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ItineraryItemId that = (ItineraryItemId) o;
        return attractionId.equals(that.attractionId) && itineraryId.equals(that.itineraryId);
    }

    @Override
    public String toString() {
        return "ItineraryItemId{" +
                "attractionId=" + attractionId +
                ", itineraryId=" + itineraryId +
                '}';
    }
}
