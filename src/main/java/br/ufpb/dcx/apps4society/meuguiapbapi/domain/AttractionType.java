package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class AttractionType {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 200, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String description;

    public AttractionType() {
    }

    public AttractionType(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
