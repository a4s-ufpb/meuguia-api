package br.ufpb.dcx.apps4society.meuguiapbapi.dto.attraction;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.attractiontype.AttractionTypeDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.moreinfolink.MoreInfoLinkDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.tourismsegmentation.TourismSegmentationDTO;

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


    private List<TourismSegmentationDTO> segmentations;

    private AttractionTypeDTO attractionType;

    private List<MoreInfoLinkDTO> moreInfoLinks;

    public AttractionDTO() {
        this(0L, "", "", "", "", "","", null, null, null);
    }

    public AttractionDTO(Attraction obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.description = obj.getDescription();
        this.mapLink = obj.getMapLink();
        this.city = obj.getCity();
        this.state = obj.getState();
        this.imageLink = obj.getImageLink();
        this.segmentations = obj.getSegmentations().stream().map(TourismSegmentationDTO::new).toList();
        this.attractionType = new AttractionTypeDTO(obj.getAttractionType());
        this.moreInfoLinks = obj.getMoreInfoLinks().stream().map(MoreInfoLinkDTO::new).toList();
    }

    public AttractionDTO(Long id, String name, String description, String mapLink, String city, String state, String imageLink, List<TourismSegmentationDTO> segmentations, AttractionTypeDTO attractionType, List<MoreInfoLinkDTO> moreInfoLinks) {
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

    public List<TourismSegmentationDTO> getSegmentations() {
        return this.segmentations;
    }

    public AttractionTypeDTO getAttractionType() {
        return this.attractionType;
    }

    public List<MoreInfoLinkDTO> getMoreInfoLinks() {
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

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setSegmentations(List<TourismSegmentationDTO> segmentations) {
        this.segmentations = segmentations;
    }

    public void setAttractionType(AttractionTypeDTO attractionType) {
        this.attractionType = attractionType;
    }

    public void setMoreInfoLinks(List<MoreInfoLinkDTO> moreInfoLinks) {
        this.moreInfoLinks = moreInfoLinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttractionDTO that = (AttractionDTO) o;
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
        return "TouristAttractionDTO(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", mapLink=" + this.getMapLink() + ", city=" + this.getCity() + ", state=" + this.getState() + ", imageLink=" + this.getImageLink() + ", segmentations=" + this.getSegmentations() + ", attractionTypes=" + this.getAttractionType() + ", moreInfoLinkList=" + this.getMoreInfoLinks() + ")";
    }

    public static class TouristAttractionDTOBuilder {
        private Long id;
        private String name;
        private String description;
        private String mapLink;
        private String city;
        private String state;
        private String imageLink;
        private List<TourismSegmentationDTO> segmentations;
        private AttractionTypeDTO attractionType;
        private List<MoreInfoLinkDTO> moreInfoLinks;

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

        public TouristAttractionDTOBuilder segmentations(List<TourismSegmentationDTO> segmentations) {
            this.segmentations = segmentations;
            return this;
        }

        public TouristAttractionDTOBuilder attractionTypes(AttractionTypeDTO attractionTypes) {
            this.attractionType = attractionTypes;
            return this;
        }

        public TouristAttractionDTOBuilder moreInfoLinks(List<MoreInfoLinkDTO> moreInfoLinks) {
            this.moreInfoLinks = moreInfoLinks;
            return this;
        }

        public AttractionDTO build() {
            return new AttractionDTO(this.id, this.name, this.description, this.mapLink, this.city, this.state, this.imageLink, this.segmentations, this.attractionType, this.moreInfoLinks);
        }

        public String toString() {
            return "TouristAttractionDTO.TouristAttractionDTOBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ", mapLink=" + this.mapLink + ", city=" + this.city + ", state=" + this.state + ", imageLink=" + this.imageLink + ", segmentations=" + this.segmentations + ", attractionTypes=" + this.attractionType + ", moreInfoLinkList=" + this.moreInfoLinks + ")";
        }
    }
}
