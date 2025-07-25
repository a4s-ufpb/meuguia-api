package br.ufpb.dcx.apps4society.meuguiapbapi.helper;

import br.ufpb.dcx.apps4society.meuguiapbapi.city.domain.City;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.dto.CityDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityTestHelper {
    @Autowired
    private CityRepository cityRepository;

    private City lastCityCreated;

    /**
     * Instantiate an object of the City class
     *
     * @return a default city object with given name and country, others attributes are null
     */
    public static City createCityObjectWithNameCountry(String name, String country) {
        return City.builder()
                .name(name)
                .country(country)
                .build();

    }

    /**
     * Instantiate an object of the City class
     * <p>id=1</p>
     * <p>name= MockCity</p>
     * <p>state= MockState</p>
     * <p>country= MockCountry</p>
     * <p>stateAbbreviation= MC</p>
     *
     * @return a default city object
     */
    public static City createDefaultCityObject() {
        return City.builder()
                .id(1L)
                .name("MockCity")
                .state("MockState")
                .country("MockCountry")
                .stateAbbreviation("MC")
                .build();
    }

    /**
     * Create a cityDto from the default city object
     * <p>id=1</p>
     * <p>name= MockCity</p>
     * <p>state= MockState</p>
     * <p>country= MockCountry</p>
     * <p>stateAbbreviation= MC</p>
     *
     * @return the cityDTO object
     */
    public static CityDTO createDefaultCityDTO() {
        return createDefaultCityObject().toDto();
    }

    /**
     * Delete the city with the given name and country.
     *
     * @param name    name of the city
     * @param country country of the city
     */
    public void deleteCityWithNameAndCountry(String name, String country) {
        this.cityRepository.deleteByNameAndCountry(name, country);
    }

    /**
     * Delete the last city created by this class.
     */
    public void deleteLastCityCreated() {
        if (this.lastCityCreated != null) {
            cityRepository.deleteById(this.lastCityCreated.getId());
        }
    }

    public City createCity() {
        City city = createDefaultCityObject();
        city.setId(null);
        this.lastCityCreated = cityRepository.save(city);
        return city;
    }
}
