package br.ufpb.dcx.apps4society.meuguiapbapi.dto.moreinfolink;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class MoreInfoLinkRequestData {
    @NotBlank(
                message = "O link não pode ser vazio"
        )
    @Pattern(
            regexp = "^(https?)://(www.)?[^\\s/$.?#].\\S*$|^www\\.\\S+$",
            message = "URL inválida"
    )
    private String link;
    @NotNull(
            message = "Objeto json deve conter o atributo 'description'"
    )
    private String description;

    public MoreInfoLinkRequestData(String link, String description) {
        this.link = link;
        this.description = description;
    }

    public MoreInfoLinkRequestData() {
    }

    public static MoreInfoLinkRequestDataBuilder builder() {
        return new MoreInfoLinkRequestDataBuilder();
    }

    public String getLink() {
        return this.link;
    }

    public String getDescription() {
        return this.description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoreInfoLinkRequestData that = (MoreInfoLinkRequestData) o;
        return Objects.equals(link, that.link) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(link);
        result = 31 * result + Objects.hashCode(description);
        return result;
    }

    public String toString() {
        return "MoreInfoLinkRequestData(link=" + this.getLink() + ", description=" + this.getDescription() + ")";
    }

    public static class MoreInfoLinkRequestDataBuilder {
        private String link;
        private String description;

        MoreInfoLinkRequestDataBuilder() {
        }

        public MoreInfoLinkRequestDataBuilder link(String link) {
            this.link = link;
            return this;
        }

        public MoreInfoLinkRequestDataBuilder description(String description) {
            this.description = description;
            return this;
        }

        public MoreInfoLinkRequestData build() {
            return new MoreInfoLinkRequestData(this.link, this.description);
        }

        public String toString() {
            return "MoreInfoLinkRequestData.MoreInfoLinkRequestDataBuilder(link=" + this.link + ", description=" + this.description + ")";
        }
    }
}
