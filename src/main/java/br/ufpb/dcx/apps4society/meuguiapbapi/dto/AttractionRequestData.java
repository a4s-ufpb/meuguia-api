package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Objects;

public class AttractionRequestData {
    @NotBlank(
            message = "Nome da Atração é obrigatório"
    )
    private String name;

    private String description;

    private String mapLink;

    @NotBlank(
            message = "Cidade da Atração é obrigatória"
    )
    private String city;

    @NotBlank(
            message = "Estado da Atração é obrigatório"
    )
    private String state;

    @NotBlank(
            message = "Link da Imagem é obrigatório"
    )
    @Pattern(
            regexp = "^(https?)://[^\\s/$.?#].\\S*$",
            message = "Link da Imagem inválido. Link deve iniciar com http ou https"
    )
    private String imageLink;

    private String infoSource;

    @NotEmpty(
            message = "Segmentações da Atração são obrigatórias"
    )
    private List<@Valid TourismSegmentation> segmentations;

    @NotNull(
            message = "Tipos de Atração são obrigatórios"
    )
    private @Valid AttractionType attractionTypes;

    @NotEmpty(
            message = "Links de Mais Informações são obrigatórios"
    )
    private List<@Valid MoreInfoLink> moreInfoLinkList;

    public AttractionRequestData(String name,
                                 String description,
                                 String mapLink,
                                 String city,
                                 String state,
                                 String imageLink,
                                 String infoSource,
                                 List<TourismSegmentation> segmentations,
                                 AttractionType attractionTypes,
                                 List<MoreInfoLink> moreInfoLinkList) {
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

    public AttractionRequestData() {
    }

    public static AttractionRequestDataBuilder builder() {
        return new AttractionRequestDataBuilder();
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

        AttractionRequestData that = (AttractionRequestData) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(mapLink, that.mapLink) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(imageLink, that.imageLink) && Objects.equals(infoSource, that.infoSource) && Objects.equals(segmentations, that.segmentations) && Objects.equals(attractionTypes, that.attractionTypes) && Objects.equals(moreInfoLinkList, that.moreInfoLinkList);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
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
        return "AttractionRequestData(name=" + this.getName() + ", description=" + this.getDescription() + ", map_link=" + this.getMapLink() + ", city=" + this.getCity() + ", state=" + this.getState() + ", image_link=" + this.getImageLink() + ", fonte=" + this.getInfoSource() + ", segmentations=" + this.getSegmentations() + ", attractionTypes=" + this.getAttractionTypes() + ", moreInfoLinkList=" + this.getMoreInfoLinkList() + ")";
    }

    public static class AttractionRequestDataBuilder {
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

        AttractionRequestDataBuilder() {
        }

        public AttractionRequestDataBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AttractionRequestDataBuilder description(String description) {
            this.description = description;
            return this;
        }

        public AttractionRequestDataBuilder mapLink(String mapLink) {
            this.mapLink = mapLink;
            return this;
        }

        public AttractionRequestDataBuilder city(String city) {
            this.city = city;
            return this;
        }

        public AttractionRequestDataBuilder state(String state) {
            this.state = state;
            return this;
        }

        public AttractionRequestDataBuilder imageLink(String imageLink) {
            this.imageLink = imageLink;
            return this;
        }

        public AttractionRequestDataBuilder infoSource(String infoSource) {
            this.infoSource = infoSource;
            return this;
        }

        public AttractionRequestDataBuilder segmentations(List<TourismSegmentation> segmentations) {
            this.segmentations = segmentations;
            return this;
        }

        public AttractionRequestDataBuilder attractionTypes(AttractionType attractionTypes) {
            this.attractionTypes = attractionTypes;
            return this;
        }

        public AttractionRequestDataBuilder moreInfoLinkList(List<MoreInfoLink> moreInfoLinkList) {
            this.moreInfoLinkList = moreInfoLinkList;
            return this;
        }

        public AttractionRequestData build() {
            return new AttractionRequestData(this.name, this.description, this.mapLink, this.city, this.state, this.imageLink, this.infoSource, this.segmentations, this.attractionTypes, this.moreInfoLinkList);
        }

        public String toString() {
            return "AttractionRequestData.AttractionRequestDataBuilder(name=" + this.name + ", description=" + this.description + ", map_link=" + this.mapLink + ", city=" + this.city + ", state=" + this.state + ", image_link=" + this.imageLink + ", fonte=" + this.infoSource + ", segmentations=" + this.segmentations + ", attractionTypes=" + this.attractionTypes + ", moreInfoLinkList=" + this.moreInfoLinkList + ")";
        }
    }
}