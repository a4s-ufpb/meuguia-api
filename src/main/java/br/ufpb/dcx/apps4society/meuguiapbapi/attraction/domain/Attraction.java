package br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.domain.City;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import jakarta.persistence.*;

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

    public Attraction(Long id, String name, String description, String mapLink, City city, String imageLink) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mapLink = mapLink;
        this.city = city;
        this.imageLink = imageLink;
    }

    public Attraction(Long id, String name, String description, String mapLink, City city, String imageLink, List<TourismSegmentation> segmentations, AttractionType attractionType, List<MoreInfoLink> moreInfoLinks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mapLink = mapLink;
        this.city = city;
        this.imageLink = imageLink;
        this.segmentations = segmentations;
        this.attractionType = attractionType;
        this.moreInfoLinks = moreInfoLinks;
    }

    public AttractionDTO toDto() {
        return new AttractionDTO(this);
    }

    public static AttractionBuilder builder() {
        return new AttractionBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMapLink() {
        return this.mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public List<TourismSegmentation> getSegmentations() {
        return this.segmentations;
    }

    public AttractionType getAttractionType() {
        return this.attractionType;
    }

    public void setAttractionType(AttractionType attractionType) {
        this.attractionType = attractionType;
    }

    public List<MoreInfoLink> getMoreInfoLinks() {
        return this.moreInfoLinks;
    }

    public void addSegmentation(TourismSegmentation segmentation) {
        this.segmentations.add(segmentation);
    }

    public void addMoreInfoLink(MoreInfoLink moreInfoLink) {
        this.moreInfoLinks.add(moreInfoLink);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(mapLink);
        result = 31 * result + Objects.hashCode(city);
        result = 31 * result + Objects.hashCode(imageLink);
        result = 31 * result + Objects.hashCode(segmentations);
        result = 31 * result + Objects.hashCode(attractionType);
        result = 31 * result + Objects.hashCode(moreInfoLinks);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attraction that = (Attraction) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(mapLink, that.mapLink) && Objects.equals(city, that.city) && Objects.equals(imageLink, that.imageLink) && Objects.equals(segmentations, that.segmentations) && Objects.equals(attractionType, that.attractionType) && Objects.equals(moreInfoLinks, that.moreInfoLinks);
    }

    public String toString() {
        return "Attraction(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", mapLink=" + this.getMapLink() + ", city=" + this.getCity().toString() + ", imageLink=" + this.getImageLink() + ", segmentations=" + this.getSegmentations().toString() + ", attractionType=" + this.getAttractionType().toString() + ", moreInfoLinkList=" + this.getMoreInfoLinks().toString() + ")";
    }

    public static class AttractionBuilder {
        private Long id;
        private String name;
        private String description;
        private String mapLink;
        private City city;
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
            return new Attraction(this.id, this.name, this.description, this.mapLink, this.city, this.imageLink, this.segmentations, this.attractionType, this.moreInfoLinks);
        }

        public String toString() {
            return "Attraction.AttractionBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ", mapLink=" + this.mapLink + ", city=" + this.city + ", imageLink=" + this.imageLink + ", segmentations=" + this.segmentations + ", attractionType=" + this.attractionType + ", moreInfoLinkList=" + this.moreInfoLinks + ")";
        }
    }
}
