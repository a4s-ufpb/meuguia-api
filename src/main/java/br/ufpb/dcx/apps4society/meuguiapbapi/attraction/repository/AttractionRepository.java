package br.ufpb.dcx.apps4society.meuguiapbapi.attraction.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {

    List<Attraction> findByNameContainingIgnoreCase(String name);

    @Query("SELECT obj FROM Attraction obj WHERE obj.city = :city")
    List<Attraction> findAllByCity(@Param(value = "city") String city);

    @Query("SELECT a FROM Attraction a JOIN a.attractionType s WHERE s.name = :attractionTypesName")
    List<Attraction> findAllByType(@Param(value = "attractionTypesName") String attractionTypes);

    @Query("SELECT a FROM Attraction a JOIN a.segmentations s WHERE s.name = :segmentationName")
    List<Attraction> findAllBySegmentationName(@Param("segmentationName") String segmentationName);

    Optional<Attraction> findByNameAndCity(String name, City city);
}
