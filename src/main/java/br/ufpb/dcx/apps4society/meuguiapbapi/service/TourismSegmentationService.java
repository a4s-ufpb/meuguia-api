package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TouristSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.TourismSegmentationRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TourismSegmentationService {

    @Autowired
    private TourismSegmentationRepository tourismSegmentationRepository;

    @Autowired
    private AttractionService attractionService;

    public TouristSegmentation findById(Long id) {
        Optional<TouristSegmentation> obj = tourismSegmentationRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! id: " + id + ", Tipo " + TouristSegmentation.class.getName()));
    }

    public List<TouristSegmentation> findAll() {
        List<TouristSegmentation> segmentations = tourismSegmentationRepository.findAll();

        // Filtra para manter apenas as segmentações com nomes únicos
        return new ArrayList<>(segmentations.stream()
                .collect(Collectors.toMap(
                        TouristSegmentation::getName,  // Usa o nome como chave
                        Function.identity(),          // Usa o próprio objeto TurismSegmentation como valor
                        (existing, replacement) -> existing // Em caso de duplicata, mantém o existente
                ))
                .values());
    }

    public TouristSegmentation create(TouristSegmentation obj) {
        obj.setId(null);
        return tourismSegmentationRepository.save(obj);
    }

    public void delete(Long id) {
        findById(id);
        tourismSegmentationRepository.deleteById(id);
    }
}
