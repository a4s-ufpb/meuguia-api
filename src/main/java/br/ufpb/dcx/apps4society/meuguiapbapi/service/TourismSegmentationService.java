package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.tourismsegmentation.TourismSegmentationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.TourismSegmentationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public List<TourismSegmentation> findAll() {
        List<TourismSegmentation> segmentations = tourismSegmentationRepository.findAll();

        // Filtra para manter apenas as segmentações com nomes únicos
        return new ArrayList<>(segmentations.stream()
                .collect(Collectors.toMap(
                        TourismSegmentation::getName,  // Usa o nome como chave
                        Function.identity(),          // Usa o próprio objeto TurismSegmentation como valor
                        (existing, replacement) -> existing // Em caso de duplicata, mantém o existente
                ))
                .values());
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
