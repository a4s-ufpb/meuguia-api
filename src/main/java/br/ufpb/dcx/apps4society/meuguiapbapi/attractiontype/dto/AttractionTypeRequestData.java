package br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class AttractionTypeRequestData {
    @NotBlank(
            message = "O nome do tipo de atrativo é obrigatório"
    )
    private String name;
    @NotNull(
            message = "Objeto json deve conter o atributo 'description'"
    )
    private String description;

    public AttractionTypeRequestData(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public AttractionTypeRequestData() {
    }

    public static AttractionTypeRequestDataBuilder builder() {
        return new AttractionTypeRequestDataBuilder();
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

        AttractionTypeRequestData that = (AttractionTypeRequestData) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    public String toString() {
        return "AttractionTypeRequestData(name=" + this.getName() + ", description=" + this.getDescription() + ")";
    }

    public static class AttractionTypeRequestDataBuilder {
        private String name;
        private String description;

        AttractionTypeRequestDataBuilder() {
        }

        public AttractionTypeRequestDataBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AttractionTypeRequestDataBuilder description(String description) {
            this.description = description;
            return this;
        }

        public AttractionTypeRequestData build() {
            return new AttractionTypeRequestData(this.name, this.description);
        }

        public String toString() {
            return "AttractionTypeRequestData.AttractionTypeRequestDataBuilder(name=" + this.name + ", description=" + this.description + ")";
        }
    }
}
