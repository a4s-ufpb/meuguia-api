package br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.domain.enums.DiffType;

import java.time.LocalDateTime;
import java.util.List;

public class AttractionDiffResponse {
    private String attractionName;
    private List<FieldDifference> differences;
    private boolean hasChanges;
    private DiffType diffType;
    private LocalDateTime comparisonTimestamp;

    public AttractionDiffResponse(String attractionName, List<FieldDifference> differences, boolean hasChanges, DiffType diffType) {
        this.attractionName = attractionName;
        this.differences = differences;
        this.hasChanges = hasChanges;
        this.diffType = diffType;
        this.comparisonTimestamp = LocalDateTime.now();
    }

    public AttractionDiffResponse(String attractionName, List<FieldDifference> differences) {
        this(attractionName, differences, !differences.isEmpty(), differences.isEmpty() ? DiffType.UNCHANGED: DiffType.CHANGED);
    }

    public DiffType getDiffType() {
        return diffType;
    }

    public void setDiffType(DiffType diffType) {
        this.diffType = diffType;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public List<FieldDifference> getDifferences() {
        return differences;
    }

    public void setDifferences(List<FieldDifference> differences) {
        this.differences = differences;
    }

    public boolean isHasChanges() {
        return hasChanges;
    }

    public void setHasChanges(boolean hasChanges) {
        this.hasChanges = hasChanges;
    }

    public LocalDateTime getComparisonTimestamp() {
        return comparisonTimestamp;
    }

    public void setComparisonTimestamp(LocalDateTime comparisonTimestamp) {
        this.comparisonTimestamp = comparisonTimestamp;
    }
}
