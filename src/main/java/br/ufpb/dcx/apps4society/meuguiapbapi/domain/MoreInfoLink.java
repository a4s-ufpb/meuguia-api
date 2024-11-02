package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;

@Data
@Builder
@Entity
public class MoreInfoLink {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 200, nullable = false)
    private String link;

    @Column(length = 200, nullable = false)
    private String description;

    public MoreInfoLink() {
    }

    public MoreInfoLink(Long id, String link, String description) {
        this.id = id;
        this.link = link;
        this.description = description;
    }
}
