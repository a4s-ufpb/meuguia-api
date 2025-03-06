package br.ufpb.dcx.apps4society.meuguiapbapi.city.service;

import aj.org.objectweb.asm.commons.Remapper;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.domain.City;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.dto.CityRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.repository.CityRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City createCity(CityRequestData cityRequestData) {
        return cityRepository.save(cityRequestData.toEntity());
    }

    public City updateCity(Long id, CityRequestData cityRequestData) {
        City city = findById(id);
        city.setName(cityRequestData.getName());
        city.setState(cityRequestData.getState());
        city.setCountry(cityRequestData.getCountry());
        city.setStateAbbreviation(cityRequestData.getStateAbbreviation());
        return cityRepository.save(city);
    }

    public City findById(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("City not founded."));
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }


    public Page<City> findAll(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }
}
