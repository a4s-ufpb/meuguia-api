package br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.domain.MoreInfoLink;

import java.util.Objects;

public class MoreInfoLinkDTO {
    private final String link;
    private final String description;

    public MoreInfoLinkDTO() {
        this("", "");
    }

    public MoreInfoLinkDTO(String link, String description) {
        this.link = link;
        this.description = description;
    }

    public MoreInfoLinkDTO(MoreInfoLink obj) {
        this.link = obj.getLink();
        this.description = obj.getDescription();
    }

    public static MoreInfoLinkDTOBuilder builder() {
        return new MoreInfoLinkDTOBuilder();
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        MoreInfoLinkDTO that = (MoreInfoLinkDTO) o;
        return Objects.equals(link, that.link) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(link);
        result = 31 * result + Objects.hashCode(description);
        return result;
    }

    @Override
    public String toString() {
        return "MoreInfoLinkDTO(" +
                "link='" + link + "'" +
                ", description='" + description + "'" +
                ")";
    }

    public static final class MoreInfoLinkDTOBuilder {
        private String link;
        private String description;

        private MoreInfoLinkDTOBuilder() {
        }

        public static MoreInfoLinkDTOBuilder aMoreInfoLinkDTO() {
            return new MoreInfoLinkDTOBuilder();
        }

        public MoreInfoLinkDTOBuilder link(String link) {
            this.link = link;
            return this;
        }

        public MoreInfoLinkDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public MoreInfoLinkDTO build() {
            return new MoreInfoLinkDTO(link, description);
        }
    }
}
