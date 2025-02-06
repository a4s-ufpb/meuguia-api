package br.ufpb.dcx.apps4society.meuguiapbapi.dto.tourismsegmentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class TourismSegmentationRequestData {
    @NotBlank(
        message = "O nome do segmento turístico é obrigatório"
    )
    private String name;

    @NotNull(
            message = "Objeto json deve conter o atributo 'description'"
    )
    private String description;

    public TourismSegmentationRequestData(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public TourismSegmentationRequestData() {
    }

    public static TourismSegmentationRequestDataBuilder builder() {
        return new TourismSegmentationRequestDataBuilder();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
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

        TourismSegmentationRequestData that = (TourismSegmentationRequestData) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        return result;
    }

    public String toString() {
        return "TourismSegmentationRequestData(name=" + this.getName() + ", description=" + this.getDescription() + ")";
    }

    public static class TourismSegmentationRequestDataBuilder {
        private String name;
        private String description;

        TourismSegmentationRequestDataBuilder() {
        }

        public TourismSegmentationRequestDataBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TourismSegmentationRequestDataBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TourismSegmentationRequestData build() {
            return new TourismSegmentationRequestData(this.name, this.description);
        }

        public String toString() {
            return "TourismSegmentationRequestData.TourismSegmentationRequestDataBuilder(name=" + this.name + ", description=" + this.description + ")";
        }
    }
}
