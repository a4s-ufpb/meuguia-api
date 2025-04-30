package br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AttractionTypeRepository extends JpaRepository<AttractionType, Long> {
    List<AttractionType> findByNameContainingIgnoreCase(String name);
}
