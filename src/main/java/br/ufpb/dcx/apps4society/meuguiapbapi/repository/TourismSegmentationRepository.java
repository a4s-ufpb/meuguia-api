package br.ufpb.dcx.apps4society.meuguiapbapi.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TourismSegmentationRepository extends JpaRepository<TourismSegmentation, Long> {
    boolean existsByName(String segmentationName);
    List<TourismSegmentation> findByNameContainingIgnoreCase(String name);
}
