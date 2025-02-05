package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tourism_segmentation")
public class TourismSegmentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "description", length = 200, nullable = false)
    private String description;

    public TourismSegmentation() {
    }

    public TourismSegmentation(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static TourismSegmentationBuilder builder() {
        return new TourismSegmentationBuilder();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TourismSegmentation that = (TourismSegmentation) o;
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
        return "TourismSegmentation(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ")";
    }

    public static class TourismSegmentationBuilder {
        private Long id;
        private String name;
        private String description;

        TourismSegmentationBuilder() {
        }

        public TourismSegmentationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TourismSegmentationBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TourismSegmentationBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TourismSegmentation build() {
            return new TourismSegmentation(this.id, this.name, this.description);
        }

        public String toString() {
            return "TourismSegmentation.TourismSegmentationBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ")";
        }
    }
}
