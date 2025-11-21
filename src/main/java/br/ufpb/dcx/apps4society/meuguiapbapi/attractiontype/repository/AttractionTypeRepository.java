package br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttractionTypeRepository extends JpaRepository<AttractionType, Long> {
    List<AttractionType> findByNameContainingIgnoreCase(String name);
    Optional<AttractionType> findByNameIgnoreCase(String name);
}
