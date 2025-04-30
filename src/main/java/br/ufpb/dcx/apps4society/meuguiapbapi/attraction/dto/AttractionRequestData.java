package br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkRequestData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

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

    @NotNull(
            message = "Cidade da Atração é obrigatória"
    )
    @Positive(
            message = "Cidade da Atração é obrigatória"
    )
    private Long cityId;

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
                                 Long cityId,
                                 String imageLink,
                                 List<Long> segmentations,
                                 Long attractionType,
                                 List<MoreInfoLinkRequestData> moreInfoLinks) {
        this.name = name;
        this.description = description;
        this.mapLink = mapLink;
        this.cityId = cityId;
        this.imageLink = imageLink;
        this.segmentations = segmentations;
        this.attractionType = attractionType;
        this.moreInfoLinks = moreInfoLinks;
    }

    public AttractionRequestData() {
        this("", "", "", 0L, "", List.of(), 0L, List.of());
    }

    public static AttractionRequestDataBuilder builder() {
        return new AttractionRequestDataBuilder();
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

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public List<Long> getSegmentations() {
        return segmentations;
    }

    public void setSegmentations(List<Long> segmentations) {
        this.segmentations = segmentations;
    }

    public Long getAttractionType() {
        return attractionType;
    }

    public void setAttractionType(Long attractionType) {
        this.attractionType = attractionType;
    }

    public List<MoreInfoLinkRequestData> getMoreInfoLinks() {
        return moreInfoLinks;
    }

    public void setMoreInfoLinks(List<MoreInfoLinkRequestData> moreInfoLinks) {
        this.moreInfoLinks = moreInfoLinks;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(mapLink);
        result = 31 * result + Objects.hashCode(cityId);
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

        AttractionRequestData that = (AttractionRequestData) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(mapLink, that.mapLink) && Objects.equals(cityId, that.cityId) && Objects.equals(imageLink, that.imageLink) && Objects.equals(segmentations, that.segmentations) && Objects.equals(attractionType, that.attractionType) && Objects.equals(moreInfoLinks, that.moreInfoLinks);
    }

    public String toString() {
        return "AttractionRequestData(name=" + this.getName() + ", description=" + this.getDescription() + ", map_link=" + this.getMapLink() + ", cityId=" + this.getCityId() + ", image_link=" + this.getImageLink() + ", segmentations=" + this.getSegmentations() + ", attractionTypes=" + this.getAttractionType() + ", moreInfoLinkList=" + this.getMoreInfoLinks() + ")";
    }

    public static final class AttractionRequestDataBuilder {
        private String name;
        private String description;
        private String mapLink;
        private Long cityId;
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

        public AttractionRequestDataBuilder cityId(Long cityId) {
            this.cityId = cityId;
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
            return new AttractionRequestData(name, description, mapLink, cityId, imageLink, segmentations, attractionType, moreInfoLinks);
        }
    }
}