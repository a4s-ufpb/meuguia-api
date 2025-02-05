package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "more_info_link")
public class MoreInfoLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "link", length = 200, nullable = false)
    private String link;

    @Column(name = "decription", length = 200, nullable = false)
    private String description;

    public MoreInfoLink() {
    }

    public MoreInfoLink(Long id, String link, String description) {
        this.id = id;
        this.link = link;
        this.description = description;
    }

    public static MoreInfoLinkBuilder builder() {
        return new MoreInfoLinkBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return Objects.equals(id, that.id) && Objects.equals(link, that.link) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(link);
        result = 31 * result + Objects.hashCode(description);
        return result;
    }

    public String toString() {
        return "MoreInfoLink(id=" + this.getId() + ", link=" + this.getLink() + ", description=" + this.getDescription() + ")";
    }

    public static class MoreInfoLinkBuilder {
        private Long id;
        private String link;
        private String description;

        MoreInfoLinkBuilder() {
        }

        public MoreInfoLinkBuilder id(Long id) {
            this.id = id;
            return this;
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
            return new MoreInfoLink(this.id, this.link, this.description);
        }

        public String toString() {
            return "MoreInfoLink.MoreInfoLinkBuilder(id=" + this.id + ", link=" + this.link + ", description=" + this.description + ")";
        }
    }
}
