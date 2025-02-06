package br.ufpb.dcx.apps4society.meuguiapbapi.dto.attraction;

import br.ufpb.dcx.apps4society.meuguiapbapi.dto.moreinfolink.MoreInfoLinkRequestData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Objects;

// TODO: Testar modificações
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
            regexp = "^(https?)://[^\\s/$.?#].\\S*$|^www\\.\\S+$",
            message = "Link da Imagem inválido. Link deve iniciar com http ou https"
    )
    private String imageLink;

    @NotNull(
            message = "Segmentações da Atração são obrigatórias"
    )
    private List<@Positive Long> segmentations;

    @NotNull(
            message = "Tipo de Atração é obrigatório"
    )
    @Positive
    private Long attractionType;

    @NotNull(
            message = "Links de Mais Informações são obrigatórios"
    )
    private List<@Valid MoreInfoLinkRequestData> moreInfoLinks;

    public AttractionRequestData(String name,
                                 String description,
                                 String mapLink,
                                 String city,
                                 String state,
                                 String imageLink,
                                 List<Long> segmentations,
                                 Long attractionType,
                                 List<MoreInfoLinkRequestData> moreInfoLinks) {
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

    public AttractionRequestData() {
        this("", "", "", "", "", "", List.of(), 0L, List.of());
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

    public List<Long> getSegmentations() {
        return segmentations;
    }

    public Long getAttractionType() {
        return attractionType;
    }

    public List<MoreInfoLinkRequestData> getMoreInfoLinks() {
        return moreInfoLinks;
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

    public void setSegmentations(List<Long> segmentations) {
        this.segmentations = segmentations;
    }

    public void setAttractionType(Long attractionType) {
        this.attractionType = attractionType;
    }

    public void setMoreInfoLinks(List<MoreInfoLinkRequestData> moreInfoLinks) {
        this.moreInfoLinks = moreInfoLinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttractionRequestData that = (AttractionRequestData) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(mapLink, that.mapLink) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(imageLink, that.imageLink) && Objects.equals(segmentations, that.segmentations) && Objects.equals(attractionType, that.attractionType) && Objects.equals(moreInfoLinks, that.moreInfoLinks);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
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
        return "AttractionRequestData(name=" + this.getName() + ", description=" + this.getDescription() + ", map_link=" + this.getMapLink() + ", city=" + this.getCity() + ", state=" + this.getState() + ", image_link=" + this.getImageLink() + ", segmentations=" + this.getSegmentations() + ", attractionTypes=" + this.getAttractionType() + ", moreInfoLinkList=" + this.getMoreInfoLinks() + ")";
    }

    public static final class AttractionRequestDataBuilder {
        private String name;
        private String description;
        private String mapLink;
        private String city;
        private String state;
        private String imageLink;
        private List<Long> segmentations;
        private Long attractionType;
        private List<MoreInfoLinkRequestData> moreInfoLinks;

        private AttractionRequestDataBuilder() {
        }

        public static AttractionRequestDataBuilder anAttractionRequestData() {
            return new AttractionRequestDataBuilder();
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

        public AttractionRequestDataBuilder segmentations(List<Long> segmentations) {
            this.segmentations = segmentations;
            return this;
        }

        public AttractionRequestDataBuilder attractionType(Long attractionType) {
            this.attractionType = attractionType;
            return this;
        }

        public AttractionRequestDataBuilder moreInfoLinks(List<MoreInfoLinkRequestData> moreInfoLinks) {
            this.moreInfoLinks = moreInfoLinks;
            return this;
        }

        public AttractionRequestData build() {
            return new AttractionRequestData(name, description, mapLink, city, state, imageLink, segmentations, attractionType, moreInfoLinks);
        }
    }
}