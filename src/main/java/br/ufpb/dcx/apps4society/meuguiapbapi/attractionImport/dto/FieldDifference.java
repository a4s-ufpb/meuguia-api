package br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.domain.enums.DiffType;

public class FieldDifference {
    private String fieldName;
    private Object csvValue;
    private Object databaseValue;
    private DiffType diffType;
    private String description;

    public FieldDifference(String fieldName, Object csvValue, Object databaseValue, DiffType diffType) {
        this.fieldName = fieldName;
        this.csvValue = csvValue;
        this.databaseValue = databaseValue;
        this.diffType = diffType;
        this.description = generateDescription();
    }

    private String generateDescription() {
        return String.format("Field '%s' differs: CSV='%s', DB='%s'",
                fieldName, csvValue, databaseValue);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getCsvValue() {
        return csvValue;
    }

    public void setCsvValue(Object csvValue) {
        this.csvValue = csvValue;
    }

    public Object getDatabaseValue() {
        return databaseValue;
    }

    public void setDatabaseValue(Object databaseValue) {
        this.databaseValue = databaseValue;
    }

    public DiffType getDiffType() {
        return diffType;
    }

    public void setDiffType(DiffType diffType) {
        this.diffType = diffType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
