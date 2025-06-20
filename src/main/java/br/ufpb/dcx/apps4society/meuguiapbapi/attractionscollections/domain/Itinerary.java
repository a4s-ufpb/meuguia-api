package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.dto.ItineraryDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.domain.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "itineraries")
public class Itinerary extends AbstractAttractionsCollection {
    private boolean isPublic;
    private boolean isRanked;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItineraryItem> items = new HashSet<>();

    public Itinerary() {
    }

    public Itinerary(String name, String description, boolean isActive, boolean isPublic, boolean isRanked) {
        super(name, description, isActive);
        this.isPublic = isPublic;
        this.isRanked = isRanked;
    }

    public boolean hasAttraction(Attraction attraction) {
        return items.stream().anyMatch(item -> item.getAttraction().equals(attraction));
    }

    public void removeAttraction(Attraction attraction) {
        items.removeIf(item -> item.getAttraction().equals(attraction));
    }

    public void addItem(ItineraryItem item) {
        item.getId().setItineraryId(getId());
        item.setItinerary(this);
        items.add(item);
    }

    public void addAttraction(Attraction attraction) {
        ItineraryItem item = new ItineraryItem(this, attraction);
        addItem(item);
    }

    public ItineraryDTO toDto() {
        return new ItineraryDTO(this);
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

    public User getOwner() {
        return createdBy;
    }

    public void setOwner(User newOwner) {
        this.createdBy = newOwner;
    }

    public Set<ItineraryItem> getItems() {
        return items;
    }

    public void setItems(Set<ItineraryItem> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        return "Itinerary{" +
                "isPublic=" + isPublic +
                ", isRanked=" + isRanked +
                ", createdBy=" + createdBy +
                ", items=" + items +
                '}';
    }
}
