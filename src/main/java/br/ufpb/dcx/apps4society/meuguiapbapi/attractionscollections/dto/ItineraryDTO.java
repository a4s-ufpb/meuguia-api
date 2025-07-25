package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain.Itinerary;

import java.util.List;

public class ItineraryDTO {
    private Long id;
    private String name;
    private String description;
    private boolean isActive;
    private boolean isPublic;
    private boolean isRanked;

    private List<ItineraryItemDTO> items;

    public ItineraryDTO() {
    }

    public ItineraryDTO(Long id, String name, String description, boolean isActive, boolean isPublic, boolean isRanked, List<ItineraryItemDTO> items) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.isPublic = isPublic;
        this.isRanked = isRanked;
        this.items = items;
    }

    public ItineraryDTO(Itinerary itinerary) {
        this(
                itinerary.getId(),
                itinerary.getName(),
                itinerary.getDescription(),
                itinerary.isActive(),
                itinerary.isPublic(),
                itinerary.isRanked(),
                itinerary.getItems().stream().map(ItineraryItemDTO::new).toList()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isRanked() {
        return isRanked;
    }

    public void setRanked(boolean ranked) {
        isRanked = ranked;
    }

    public List<ItineraryItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItineraryItemDTO> items) {
        this.items = items;
    }
}
