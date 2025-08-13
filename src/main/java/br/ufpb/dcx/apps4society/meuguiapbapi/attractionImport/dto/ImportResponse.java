package br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.domain.enums.ImportStatus;

public class ImportResponse {
    private String attractionName;
    private ImportStatus status;
    private String message;

    public ImportResponse(String attractionName, ImportStatus status, String message) {
        this.attractionName = attractionName;
        this.status = status;
        this.message = message;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public ImportStatus getStatus() {
        return status;
    }

    public void setStatus(ImportStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
