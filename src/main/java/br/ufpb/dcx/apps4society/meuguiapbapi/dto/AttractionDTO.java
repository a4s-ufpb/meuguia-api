package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;

import java.util.List;
import java.util.Objects;

public class AttractionDTO {

    private Long id;

    private String name;

    private String description;

    private String mapLink;

    private String city;

    private String state;

    private String imageLink;

    private String infoSource;

    private List<TourismSegmentation> segmentations;

    private AttractionType attractionTypes;

    private List<MoreInfoLink> moreInfoLinkList;

    public AttractionDTO() {
    }

    public AttractionDTO(Attraction obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.description = obj.getDescription();
        this.mapLink = obj.getMapLink();
        this.city = obj.getCity();
        this.state = obj.getState();
        this.imageLink = obj.getImageLink();
        this.infoSource = obj.getInfoSource();
        this.segmentations = obj.getSegmentations();
        this.attractionTypes = obj.getAttractionType();
        this.moreInfoLinkList = obj.getMoreInfoLinkList();
    }

    public AttractionDTO(Long id, String name, String description, String mapLink, String city, String state, String imageLink, String infoSource, List<TourismSegmentation> segmentations, AttractionType attractionTypes, List<MoreInfoLink> moreInfoLinkList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mapLink = mapLink;
        this.city = city;
        this.state = state;
        this.imageLink = imageLink;
        this.infoSource = infoSource;
        this.segmentations = segmentations;
        this.attractionTypes = attractionTypes;
        this.moreInfoLinkList = moreInfoLinkList;
    }

    public static TouristAttractionDTOBuilder builder() {
        return new TouristAttractionDTOBuilder();
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

    public AttractionType getAttractionTypes() {
        return this.attractionTypes;
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

    public void setAttractionTypes(AttractionType attractionTypes) {
        this.attractionTypes = attractionTypes;
    }

    public void setMoreInfoLinkList(List<MoreInfoLink> moreInfoLinkList) {
        this.moreInfoLinkList = moreInfoLinkList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttractionDTO that = (AttractionDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(mapLink, that.mapLink) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(imageLink, that.imageLink) && Objects.equals(infoSource, that.infoSource) && Objects.equals(segmentations, that.segmentations) && Objects.equals(attractionTypes, that.attractionTypes) && Objects.equals(moreInfoLinkList, that.moreInfoLinkList);
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
        result = 31 * result + Objects.hashCode(attractionTypes);
        result = 31 * result + Objects.hashCode(moreInfoLinkList);
        return result;
    }

    public String toString() {
        return "TouristAttractionDTO(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", mapLink=" + this.getMapLink() + ", city=" + this.getCity() + ", state=" + this.getState() + ", imageLink=" + this.getImageLink() + ", infoSource=" + this.getInfoSource() + ", segmentations=" + this.getSegmentations() + ", attractionTypes=" + this.getAttractionTypes() + ", moreInfoLinkList=" + this.getMoreInfoLinkList() + ")";
    }

    public static class TouristAttractionDTOBuilder {
        private Long id;
        private String name;
        private String description;
        private String mapLink;
        private String city;
        private String state;
        private String imageLink;
        private String infoSource;
        private List<TourismSegmentation> segmentations;
        private AttractionType attractionTypes;
        private List<MoreInfoLink> moreInfoLinkList;

        TouristAttractionDTOBuilder() {
        }

        public TouristAttractionDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TouristAttractionDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TouristAttractionDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TouristAttractionDTOBuilder mapLink(String mapLink) {
            this.mapLink = mapLink;
            return this;
        }

        public TouristAttractionDTOBuilder city(String city) {
            this.city = city;
            return this;
        }

        public TouristAttractionDTOBuilder state(String state) {
            this.state = state;
            return this;
        }

        public TouristAttractionDTOBuilder imageLink(String imageLink) {
            this.imageLink = imageLink;
            return this;
        }

        public TouristAttractionDTOBuilder infoSource(String infoSource) {
            this.infoSource = infoSource;
            return this;
        }

        public TouristAttractionDTOBuilder segmentations(List<TourismSegmentation> segmentations) {
            this.segmentations = segmentations;
            return this;
        }

        public TouristAttractionDTOBuilder attractionTypes(AttractionType attractionTypes) {
            this.attractionTypes = attractionTypes;
            return this;
        }

        public TouristAttractionDTOBuilder moreInfoLinkList(List<MoreInfoLink> moreInfoLinkList) {
            this.moreInfoLinkList = moreInfoLinkList;
            return this;
        }

        public AttractionDTO build() {
            return new AttractionDTO(this.id, this.name, this.description, this.mapLink, this.city, this.state, this.imageLink, this.infoSource, this.segmentations, this.attractionTypes, this.moreInfoLinkList);
        }

        public String toString() {
            return "TouristAttractionDTO.TouristAttractionDTOBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ", mapLink=" + this.mapLink + ", city=" + this.city + ", state=" + this.state + ", imageLink=" + this.imageLink + ", infoSource=" + this.infoSource + ", segmentations=" + this.segmentations + ", attractionTypes=" + this.attractionTypes + ", moreInfoLinkList=" + this.moreInfoLinkList + ")";
        }
    }
}
