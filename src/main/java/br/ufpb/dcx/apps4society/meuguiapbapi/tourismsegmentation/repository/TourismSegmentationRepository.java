package br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourismSegmentationRepository extends JpaRepository<TourismSegmentation, Long> {
    boolean existsByName(String segmentationName);

    List<TourismSegmentation> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT ts FROM TourismSegmentation ts")
    Page<TourismSegmentation> findAllDistinct(Pageable pageable);
}
