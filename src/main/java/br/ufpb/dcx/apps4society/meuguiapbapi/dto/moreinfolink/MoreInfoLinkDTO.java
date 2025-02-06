package br.ufpb.dcx.apps4society.meuguiapbapi.dto.moreinfolink;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;

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
