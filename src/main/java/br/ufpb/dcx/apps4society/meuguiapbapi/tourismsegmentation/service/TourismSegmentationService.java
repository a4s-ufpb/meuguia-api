package br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.repository.TourismSegmentationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourismSegmentationService {

    private final TourismSegmentationRepository tourismSegmentationRepository;

    @Autowired
    public TourismSegmentationService(TourismSegmentationRepository tourismSegmentationRepository) {
        this.tourismSegmentationRepository = tourismSegmentationRepository;
    }

    public TourismSegmentation findById(Long id) {
        Optional<TourismSegmentation> obj = tourismSegmentationRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! id: " + id + ", Tipo " + TourismSegmentation.class.getName()));
    }

    public Page<TourismSegmentation> findAll(Pageable pageable) {
        return tourismSegmentationRepository.findAllDistinct(pageable);
    }

    public TourismSegmentation create(TourismSegmentationRequestData obj) {
        TourismSegmentation tourismSegmentation = TourismSegmentation.builder()
                .name(obj.getName())
                .description(obj.getDescription())
                .build();
        return tourismSegmentationRepository.save(tourismSegmentation);
    }

    public List<TourismSegmentation> searchByName(String name) {
        return tourismSegmentationRepository.findByNameContainingIgnoreCase(name);
    }

    public void delete(Long id) {
        findById(id);
        tourismSegmentationRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return tourismSegmentationRepository.existsById(id);
    }

    public boolean existsByName(String segmentationName) {

        return tourismSegmentationRepository.existsByName(segmentationName);
    }
}
