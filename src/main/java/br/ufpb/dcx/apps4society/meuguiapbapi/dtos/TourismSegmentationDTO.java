package br.ufpb.dcx.apps4society.meuguiapbapi.dtos;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import lombok.Data;

@Data
public class TourismSegmentationDTO {

    private Long id;

    private String name;

    private String description;

    public TourismSegmentationDTO() {
    }

    public TourismSegmentationDTO(TourismSegmentation obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.description = obj.getDescription();
    }
}
