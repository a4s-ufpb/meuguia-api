package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
public class TouristSegmentation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 200, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String description;

    public TouristSegmentation() {
    }

    public TouristSegmentation(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
