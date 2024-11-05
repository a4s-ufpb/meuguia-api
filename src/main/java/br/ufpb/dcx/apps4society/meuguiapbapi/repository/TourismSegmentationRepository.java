package br.ufpb.dcx.apps4society.meuguiapbapi.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourismSegmentationRepository extends JpaRepository<TourismSegmentation, Long> {
    boolean existsByName(String segmentationName);
}
