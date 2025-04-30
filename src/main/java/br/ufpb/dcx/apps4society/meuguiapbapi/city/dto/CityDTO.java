package br.ufpb.dcx.apps4society.meuguiapbapi.city.dto;

import java.util.Objects;

public class CityDTO {
    private Long id;
    private String name;
    private String state;
    private String country;
    private String stateAbbreviation;

    public CityDTO() {
    }

    public CityDTO(Long id, String name, String state, String country, String stateAbbreviation) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.country = country;
        this.stateAbbreviation = stateAbbreviation;
    }

    public CityDTOBuilder builder() {
        return new CityDTOBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(state);
        result = 31 * result + Objects.hashCode(country);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        CityDTO cityDTO = (CityDTO) o;
        return Objects.equals(id, cityDTO.id) && Objects.equals(name, cityDTO.name) && Objects.equals(state, cityDTO.state) && Objects.equals(country, cityDTO.country);
    }

    @Override
    public String toString() {
        return "CityDTO(" +
                "id=" + id +
                ", name=" + name +
                ", state=" + state +
                ", country=" + country +
                ", stateAbbreviation=" + stateAbbreviation +
                ')';
    }

    public static final class CityDTOBuilder {
        private Long id;
        private String name;
        private String state;
        private String country;
        private String stateAbbreviation;

        private CityDTOBuilder() {
        }

        public static CityDTOBuilder aCityDTO() {
            return new CityDTOBuilder();
        }

        public CityDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CityDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CityDTOBuilder state(String state) {
            this.state = state;
            return this;
        }

        public CityDTOBuilder country(String country) {
            this.country = country;
            return this;
        }

        public CityDTOBuilder stateAbbreviation(String stateAbbreviation) {
            this.stateAbbreviation = stateAbbreviation;
            return this;
        }

        public CityDTO build() {
            return new CityDTO(id, name, state, country, stateAbbreviation);
        }
    }
}
