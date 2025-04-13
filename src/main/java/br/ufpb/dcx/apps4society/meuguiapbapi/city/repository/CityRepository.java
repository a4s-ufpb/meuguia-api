package br.ufpb.dcx.apps4society.meuguiapbapi.city.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.city.domain.City;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
    @Transactional
    void deleteByNameAndCountry(String name, String country);
}
