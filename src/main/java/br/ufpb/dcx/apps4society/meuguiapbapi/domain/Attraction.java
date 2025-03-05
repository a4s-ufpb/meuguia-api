package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "attraction")
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "description", length = 100000, nullable = false)
    private String description;

    @Column(name = "map_link", length = 200, nullable = false)
    private String mapLink;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "state", length = 200, nullable = false)
    private String state;

    @Column(name = "image_link", length = 500, nullable = false)
    private String imageLink;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "attraction_tourism_segmentation",
            joinColumns = @JoinColumn(name = "attraction_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tourism_segmentation_id", referencedColumnName = "id"))
    private List<TourismSegmentation> segmentations;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private AttractionType attractionType;

    @ElementCollection
    @CollectionTable(
            name = "more_info_link",
            joinColumns = @JoinColumn(name = "attraction_id")
    )
    private List<MoreInfoLink> moreInfoLinks;

    public Attraction() {
    }

    public Attraction(Long id, String name, String description, String mapLink, City city, String state, String imageLink) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mapLink = mapLink;
        this.city = city;
        this.state = state;
        this.imageLink = imageLink;
    }

    public Attraction(Long id, String name, String description, String mapLink, City city, String state, String imageLink, List<TourismSegmentation> segmentations, AttractionType attractionType, List<MoreInfoLink> moreInfoLinks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mapLink = mapLink;
        this.city = city;
        this.state = state;
        this.imageLink = imageLink;
        this.segmentations = segmentations;
        this.attractionType = attractionType;
        this.moreInfoLinks = moreInfoLinks;
    }

    public static AttractionBuilder builder() {
        return new AttractionBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getMapLink() {
        return this.mapLink;
    }

    public City getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public List<TourismSegmentation> getSegmentations() {
        return this.segmentations;
    }

    public AttractionType getAttractionType() {
        return this.attractionType;
    }

    public List<MoreInfoLink> getMoreInfoLinks() {
        return this.moreInfoLinks;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setAttractionType(AttractionType attractionType) {
        this.attractionType = attractionType;
    }

    public void addSegmentation(TourismSegmentation segmentation) {
        this.segmentations.add(segmentation);
    }

    public void addMoreInfoLink(MoreInfoLink moreInfoLink) {
        this.moreInfoLinks.add(moreInfoLink);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attraction that = (Attraction) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(mapLink, that.mapLink) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(imageLink, that.imageLink) && Objects.equals(segmentations, that.segmentations) && Objects.equals(attractionType, that.attractionType) && Objects.equals(moreInfoLinks, that.moreInfoLinks);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(mapLink);
        result = 31 * result + Objects.hashCode(city);
        result = 31 * result + Objects.hashCode(state);
        result = 31 * result + Objects.hashCode(imageLink);
        result = 31 * result + Objects.hashCode(segmentations);
        result = 31 * result + Objects.hashCode(attractionType);
        result = 31 * result + Objects.hashCode(moreInfoLinks);
        return result;
    }

    public String toString() {
        return "Attraction(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", mapLink=" + this.getMapLink() + ", city=" + this.getCity() + ", state=" + this.getState() + ", imageLink=" + this.getImageLink() + ", segmentations=" + this.getSegmentations() + ", attractionType=" + this.getAttractionType() + ", moreInfoLinkList=" + this.getMoreInfoLinks() + ")";
    }

    public static class AttractionBuilder {
        private Long id;
        private String name;
        private String description;
        private String mapLink;
        private City city;
        private String state;
        private String imageLink;
        private List<TourismSegmentation> segmentations;
        private AttractionType attractionType;
        private List<MoreInfoLink> moreInfoLinks;

        AttractionBuilder() {
        }

        public AttractionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AttractionBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AttractionBuilder description(String description) {
            this.description = description;
            return this;
        }

        public AttractionBuilder mapLink(String mapLink) {
            this.mapLink = mapLink;
            return this;
        }

        public AttractionBuilder city(City city) {
            this.city = city;
            return this;
        }

        public AttractionBuilder state(String state) {
            this.state = state;
            return this;
        }

        public AttractionBuilder imageLink(String imageLink) {
            this.imageLink = imageLink;
            return this;
        }

        public AttractionBuilder segmentations(List<TourismSegmentation> segmentations) {
            this.segmentations = segmentations;
            return this;
        }

        public AttractionBuilder attractionType(AttractionType attractionType) {
            this.attractionType = attractionType;
            return this;
        }

        public AttractionBuilder moreInfoLinks(List<MoreInfoLink> moreInfoLinks) {
            this.moreInfoLinks = moreInfoLinks;
            return this;
        }

        public Attraction build() {
            return new Attraction(this.id, this.name, this.description, this.mapLink, this.city, this.state, this.imageLink, this.segmentations, this.attractionType, this.moreInfoLinks);
        }

        public String toString() {
            return "Attraction.AttractionBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ", mapLink=" + this.mapLink + ", city=" + this.city + ", state=" + this.state + ", imageLink=" + this.imageLink + ", segmentations=" + this.segmentations + ", attractionType=" + this.attractionType + ", moreInfoLinkList=" + this.moreInfoLinks + ")";
        }
    }
}
