package br.ufpb.dcx.apps4society.meuguiapbapi.city.dto;

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
