package br.ufpb.dcx.apps4society.meuguiapbapi.attraction.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long>, JpaSpecificationExecutor<Attraction> {
    Optional<Attraction> findByNameAndCityId(String name, Long cityId);
}
