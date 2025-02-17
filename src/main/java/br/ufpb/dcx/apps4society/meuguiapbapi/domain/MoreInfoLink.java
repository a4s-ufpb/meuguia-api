package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.dto.moreinfolink.MoreInfoLinkRequestData;
import jakarta.persistence.*;

import java.util.Objects;

@Embeddable
@Table(name = "more_info_link")
public class MoreInfoLink {

    @Column(name = "link", length = 200, nullable = false)
    private String link;

    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "attraction_id", insertable = false, updatable = false)
    private Attraction attraction;


    public MoreInfoLink(MoreInfoLinkRequestData dto) {
        this(dto.getLink(), dto.getDescription());
    }

    public MoreInfoLink() {
        this("", "");
    }

    public MoreInfoLink(String link, String description) {
        this.link = link;
        this.description = description;
    }

    public static MoreInfoLinkBuilder builder() {
        return new MoreInfoLinkBuilder();
    }


    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoreInfoLink that = (MoreInfoLink) o;
        return Objects.equals(link, that.link) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(link);
        result = 31 * result + Objects.hashCode(description);
        return result;
    }

    public String toString() {
        return "MoreInfoLink(link=" + this.getLink() + ", description=" + this.getDescription() + ")";
    }

    public static class MoreInfoLinkBuilder {
        private String link;
        private String description;

        MoreInfoLinkBuilder() {
        }

        public MoreInfoLinkBuilder link(String link) {
            this.link = link;
            return this;
        }

        public MoreInfoLinkBuilder description(String description) {
            this.description = description;
            return this;
        }

        public MoreInfoLink build() {
            return new MoreInfoLink(this.link, this.description);
        }

        public String toString() {
            return "MoreInfoLink.MoreInfoLinkBuilder(link=" + this.link + ", description=" + this.description + ")";
        }
    }
}
