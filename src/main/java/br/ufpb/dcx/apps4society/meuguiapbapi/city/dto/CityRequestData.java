package br.ufpb.dcx.apps4society.meuguiapbapi.city.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.city.domain.City;
import jakarta.validation.constraints.NotEmpty;

public class CityRequestData {
    @NotEmpty(
            message = "Nome (name) da cidade é obrigatório"
    )
    private String name;
    @NotEmpty(
            message = "Estado (state) da cidade é obrigatório"
    )
    private String state;
    @NotEmpty(
            message = "Pais (country) da cidade é obrigatório"
    )
    private String country;
    @NotEmpty(
            message = "Chave 'stateAbbreviation' é obrigatória"
    )
    private String stateAbbreviation;

    public CityRequestData() {
    }

    public CityRequestData(String name, String state, String country, String stateAbbreviation) {
        this.name = name;
        this.state = state;
        this.country = country;
        this.stateAbbreviation = stateAbbreviation;
    }

    public City toEntity() {
        return City.builder()
                .name(name)
                .state(state)
                .country(country)
                .stateAbbreviation(stateAbbreviation)
                .build();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }
}
