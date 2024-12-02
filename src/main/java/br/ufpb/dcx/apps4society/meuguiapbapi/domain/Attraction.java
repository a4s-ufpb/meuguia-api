package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "attraction")
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "description", length = 100000, nullable = false)
    private String description;

    @Column(name = "map_link", length = 200, nullable = false)
    private String mapLink;

    @Column(name = "city", length = 200, nullable = false)
    private String city;

    @Column(name = "state", length = 200, nullable = false)
    private String state;

    @Column(name = "image_link", length = 500, nullable = false)
    private String imageLink;

    @Column(name = "info_source", length = 300, nullable = false)
    private String infoSource;

    @ManyToMany
    @JoinTable(
        name = "attraction_segmentation",
        joinColumns = @JoinColumn(name = "attraction_id"),
        inverseJoinColumns = @JoinColumn(name = "segmentation_id")
    )
    private List<TourismSegmentation> segmentations = new ArrayList<>();

    @OneToOne
    @JoinTable(
        name = "attraction_attraction_type",
        joinColumns = @JoinColumn(name = "attraction_id"),
        inverseJoinColumns = @JoinColumn(name = "attraction_type_id")
    )
    private AttractionType attractionType;

    @OneToMany
    @JoinColumn(name = "attraction_id")
    private List<MoreInfoLink> moreInfoLinkList = new ArrayList<>();

    public Attraction() {
    }

    public Attraction(Long id, String name, String description, String mapLink, String city, String state, String imageLink, String infoSource) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mapLink = mapLink;
        this.city = city;
        this.state = state;
        this.imageLink = imageLink;
        this.infoSource = infoSource;
    }

    public Attraction(Long id, String name, String description, String mapLink, String city, String state, String imageLink, String infoSource, List<TourismSegmentation> segmentations, AttractionType attractionType, List<MoreInfoLink> moreInfoLinkList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mapLink = mapLink;
        this.city = city;
        this.state = state;
        this.imageLink = imageLink;
        this.infoSource = infoSource;
        this.segmentations = segmentations;
        this.attractionType = attractionType;
        this.moreInfoLinkList = moreInfoLinkList;
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

    public String getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public String getInfoSource() {
        return this.infoSource;
    }

    public List<TourismSegmentation> getSegmentations() {
        return this.segmentations;
    }

    public AttractionType getAttractionType() {
        return this.attractionType;
    }

    public List<MoreInfoLink> getMoreInfoLinkList() {
        return this.moreInfoLinkList;
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

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setInfoSource(String infoSource) {
        this.infoSource = infoSource;
    }

    public void setSegmentations(List<TourismSegmentation> segmentations) {
        this.segmentations = segmentations;
    }

    public void setAttractionType(AttractionType attractionType) {
        this.attractionType = attractionType;
    }

    public void setMoreInfoLinkList(List<MoreInfoLink> moreInfoLinkList) {
        this.moreInfoLinkList = moreInfoLinkList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attraction that = (Attraction) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(mapLink, that.mapLink) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(imageLink, that.imageLink) && Objects.equals(infoSource, that.infoSource) && Objects.equals(segmentations, that.segmentations) && Objects.equals(attractionType, that.attractionType) && Objects.equals(moreInfoLinkList, that.moreInfoLinkList);
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
        result = 31 * result + Objects.hashCode(infoSource);
        result = 31 * result + Objects.hashCode(segmentations);
        result = 31 * result + Objects.hashCode(attractionType);
        result = 31 * result + Objects.hashCode(moreInfoLinkList);
        return result;
    }

    public String toString() {
        return "Attraction(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", mapLink=" + this.getMapLink() + ", city=" + this.getCity() + ", state=" + this.getState() + ", imageLink=" + this.getImageLink() + ", infoSource=" + this.getInfoSource() + ", segmentations=" + this.getSegmentations() + ", attractionType=" + this.getAttractionType() + ", moreInfoLinkList=" + this.getMoreInfoLinkList() + ")";
    }

    public static class AttractionBuilder {
        private Long id;
        private String name;
        private String description;
        private String mapLink;
        private String city;
        private String state;
        private String imageLink;
        private String infoSource;
        private List<TourismSegmentation> segmentations;
        private AttractionType attractionType;
        private List<MoreInfoLink> moreInfoLinkList;

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

        public AttractionBuilder city(String city) {
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

        public AttractionBuilder infoSource(String infoSource) {
            this.infoSource = infoSource;
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

        public AttractionBuilder moreInfoLinkList(List<MoreInfoLink> moreInfoLinkList) {
            this.moreInfoLinkList = moreInfoLinkList;
            return this;
        }

        public Attraction build() {
            return new Attraction(this.id, this.name, this.description, this.mapLink, this.city, this.state, this.imageLink, this.infoSource, this.segmentations, this.attractionType, this.moreInfoLinkList);
        }

        public String toString() {
            return "Attraction.AttractionBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ", mapLink=" + this.mapLink + ", city=" + this.city + ", state=" + this.state + ", imageLink=" + this.imageLink + ", infoSource=" + this.infoSource + ", segmentations=" + this.segmentations + ", attractionType=" + this.attractionType + ", moreInfoLinkList=" + this.moreInfoLinkList + ")";
        }
    }
}
