package br.ufpb.dcx.apps4society.meuguiapbapi.dto.tourismsegmentation;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;

import java.util.Objects;

public class TourismSegmentationDTO {

    private Long id;

    private String name;

    private String description;

    public TourismSegmentationDTO() {
        this(0L, "", "");
    }

    public TourismSegmentationDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public TourismSegmentationDTO(TourismSegmentation obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.description = obj.getDescription();
    }

    public static TourismSegmentationDTOBuilder builder() {
        return new TourismSegmentationDTOBuilder();
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TourismSegmentationDTO that = (TourismSegmentationDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        return result;
    }

    public String toString() {
        return "TourismSegmentationDTO(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ")";
    }

    public static class TourismSegmentationDTOBuilder {
        private Long id;
        private String name;
        private String description;

        TourismSegmentationDTOBuilder() {
        }

        public TourismSegmentationDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TourismSegmentationDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TourismSegmentationDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TourismSegmentationDTO build() {
            return new TourismSegmentationDTO(this.id, this.name, this.description);
        }

        public String toString() {
            return "TourismSegmentationDTO.TourismSegmentationDTOBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ")";
        }
    }
}
