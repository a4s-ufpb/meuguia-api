package br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "attraction_type")
public class AttractionType {

    // TODO: getter and setter
    @OneToMany(mappedBy = "attractionType", fetch = FetchType.LAZY)
    private final List<Attraction> attractions = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", length = 200, nullable = false)
    private String name;
    @Column(name = "description", length = 200, nullable = false)
    private String description;

    public AttractionType() {
        this(0L, "", "");
    }

    public AttractionType(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static AttractionTypeBuilder builder() {
        return new AttractionTypeBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttractionType that = (AttractionType) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    public String toString() {
        return "AttractionType(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ")";
    }

    public static class AttractionTypeBuilder {
        private Long id;
        private String name;
        private String description;

        AttractionTypeBuilder() {
        }

        public AttractionTypeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AttractionTypeBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AttractionTypeBuilder description(String description) {
            this.description = description;
            return this;
        }

        public AttractionType build() {
            return new AttractionType(this.id, this.name, this.description);
        }

        public String toString() {
            return "AttractionType.AttractionTypeBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ")";
        }
    }
}
