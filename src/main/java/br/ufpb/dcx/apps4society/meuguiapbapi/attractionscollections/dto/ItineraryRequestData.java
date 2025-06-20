package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ItineraryRequestData {
    @NotBlank(message = "'name' cannot be blank")
    private String name;

    @NotNull(message = "'Description' cannot be null")
    private String description;

    @NotNull(message = "'public' status cannot be null")
    private boolean isPublic;

    @NotNull(message = "'ranked' status cannot be null")
    private boolean isRanked;

    @NotNull(message = "'items' cannot be null")
    private List<Long> items;

    public ItineraryRequestData() {
    }

    public ItineraryRequestData(String name, String description, boolean isPublic, boolean isRanked, List<Long> items) {
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.isRanked = isRanked;
        this.items = items;
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

    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ItineraryRequestData{" +
                "name=" + name +
                ", description=" + description +
                ", isPublic=" + isPublic +
                ", isRanked=" + isRanked +
                ", items=" + items +
                '}';
    }
}
