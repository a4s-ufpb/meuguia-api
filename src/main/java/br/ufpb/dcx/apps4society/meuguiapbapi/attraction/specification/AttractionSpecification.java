package br.ufpb.dcx.apps4society.meuguiapbapi.attraction.specification;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class AttractionSpecification implements Specification<Attraction> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private String name;
    private List<Long> segmentation;
    private List<Long> type;
    private List<Long> city;

    public AttractionSpecification() {
    }

    public AttractionSpecification(String name, List<Long> segmentation, List<Long> type, List<Long> city) {
        this.name = name;
        this.segmentation = segmentation;
        this.type = type;
        this.city = city;
    }

    @Override
    public Predicate toPredicate(Root<Attraction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();

        // Filtra por nome se não for nulo e não estiver vazio
        if (name != null && !name.isBlank()) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + name.toLowerCase() + "%"
                    )
            );
        }

        // Filtra por segmentação se a lista não for nula e não estiver vazia
        if (segmentation != null && !segmentation.isEmpty()) {
            // Usamos join para relacionamentos many-to-many
            var segmentationJoin = root.join("segmentations");
            predicate = criteriaBuilder.and(predicate,
                    segmentationJoin.get("id").in(segmentation)
            );
        }

        // Filtra por tipo de atração se a lista não for nula e não estiver vazia
        if (type != null && !type.isEmpty()) {
            predicate = criteriaBuilder.and(predicate,
                    root.get("attractionType").get("id").in(type)
            );
        }

        // Filtra por cidade se a lista não for nula e não estiver vazia
        if (city != null && !city.isEmpty()) {
            predicate = criteriaBuilder.and(predicate,
                    root.get("city").get("id").in(city)
            );
        }

        log.debug("Generated Predicate: {}", predicate);
        log.debug("Expressions: {}", predicate.getExpressions());

        return predicate;
    }

    @Override
    public String toString() {
        return "AttractionSpecification{" +
                "name=" + name +
                ", segmentation=" + segmentation +
                ", type=" + type +
                ", city=" + city +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getSegmentation() {
        return segmentation;
    }

    public void setSegmentation(List<Long> segmentation) {
        this.segmentation = segmentation;
    }

    public List<Long> getType() {
        return type;
    }

    public void setType(List<Long> type) {
        this.type = type;
    }

    public List<Long> getCity() {
        return city;
    }

    public void setCity(List<Long> city) {
        this.city = city;
    }
}
