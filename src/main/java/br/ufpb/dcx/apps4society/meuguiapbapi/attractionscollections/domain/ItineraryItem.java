package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import jakarta.persistence.*;

@Entity
public class ItineraryItem {
    @EmbeddedId
    private ItineraryItemId id;

    @ManyToOne
    @MapsId("itineraryId")
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;

    @ManyToOne
    @MapsId("attractionId")
    @JoinColumn(name = "attraction_id")
    private Attraction attraction;

    private Integer position;

    private Integer rating;

    public ItineraryItem() {
    }

    public ItineraryItem(Itinerary itinerary, Attraction attraction, Integer position) {
        this.id = new ItineraryItemId(attraction, itinerary);
        this.itinerary = itinerary;
        this.attraction = attraction;
        this.position = position;
    }

    public ItineraryItem(Itinerary itinerary, Attraction attraction) {
        this.id = new ItineraryItemId(attraction, itinerary);
        this.itinerary = itinerary;
        this.attraction = attraction;
        this.position = null;
    }

    public ItineraryItemId getId() {
        return id;
    }

    public void setId(ItineraryItemId id) {
        this.id = id;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
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
}

