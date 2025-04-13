package br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.dto.AttractionTypeDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.dto.CityDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationDTO;

import java.util.List;
import java.util.Objects;

public class AttractionDTO {

    private Long id;

    private String name;

    private String description;

    private String mapLink;

    private CityDTO city;

    private String imageLink;

    private List<TourismSegmentationDTO> segmentations;

    private AttractionTypeDTO attractionType;

    private List<MoreInfoLinkDTO> moreInfoLinks;

    public AttractionDTO() {
        this(0L, "", "", "", null, "", null, null, null);
    }

    public AttractionDTO(Attraction obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.description = obj.getDescription();
        this.mapLink = obj.getMapLink();
        this.city = obj.getCity().toDto();
        this.imageLink = obj.getImageLink();
        this.segmentations = obj.getSegmentations().stream().map(TourismSegmentationDTO::new).toList();
        this.attractionType = new AttractionTypeDTO(obj.getAttractionType());
        this.moreInfoLinks = obj.getMoreInfoLinks().stream().map(MoreInfoLinkDTO::new).toList();
    }

    public AttractionDTO(Long id, String name, String description, String mapLink, CityDTO city, String imageLink, List<TourismSegmentationDTO> segmentations, AttractionTypeDTO attractionType, List<MoreInfoLinkDTO> moreInfoLinks) {
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

    public static TouristAttractionDTOBuilder builder() {
        return new TouristAttractionDTOBuilder();
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

    public CityDTO getCity() {
        return this.city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public List<TourismSegmentationDTO> getSegmentations() {
        return this.segmentations;
    }

    public void setSegmentations(List<TourismSegmentationDTO> segmentations) {
        this.segmentations = segmentations;
    }

    public AttractionTypeDTO getAttractionType() {
        return this.attractionType;
    }

    public void setAttractionType(AttractionTypeDTO attractionType) {
        this.attractionType = attractionType;
    }

    public List<MoreInfoLinkDTO> getMoreInfoLinks() {
        return this.moreInfoLinks;
    }

    public void setMoreInfoLinks(List<MoreInfoLinkDTO> moreInfoLinks) {
        this.moreInfoLinks = moreInfoLinks;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(city);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        AttractionDTO that = (AttractionDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(city, that.city);
    }

    public String toString() {
        return "TouristAttractionDTO(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", mapLink=" + this.getMapLink() + ", city=" + this.getCity() + ", imageLink=" + this.getImageLink() + ", segmentations=" + this.getSegmentations() + ", attractionTypes=" + this.getAttractionType() + ", moreInfoLinkList=" + this.getMoreInfoLinks() + ")";
    }

    public static class TouristAttractionDTOBuilder {
        private Long id;
        private String name;
        private String description;
        private String mapLink;
        private CityDTO city;
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

        public TouristAttractionDTOBuilder city(CityDTO city) {
            this.city = city;
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
            return new AttractionDTO(this.id, this.name, this.description, this.mapLink, this.city, this.imageLink, this.segmentations, this.attractionType, this.moreInfoLinks);
        }

        public String toString() {
            return "TouristAttractionDTO.TouristAttractionDTOBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ", mapLink=" + this.mapLink + ", city=" + this.city + ", imageLink=" + this.imageLink + ", segmentations=" + this.segmentations + ", attractionTypes=" + this.attractionType + ", moreInfoLinkList=" + this.moreInfoLinks + ")";
        }
    }
}
