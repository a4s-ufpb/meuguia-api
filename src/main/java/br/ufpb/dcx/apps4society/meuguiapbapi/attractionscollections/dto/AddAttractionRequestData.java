package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.dto;

import jakarta.validation.constraints.NotNull;

public class AddAttractionRequestData {
    @NotNull(message = "'attractionId' cannot be null")
    private Long attractionId;

    private Integer position;

    public AddAttractionRequestData(Long attractionId, Integer position) {
        this.attractionId = attractionId;
        this.position = position;
    }

    public Long getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(Long attractionId) {
        this.attractionId = attractionId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
