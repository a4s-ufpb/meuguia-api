package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionTypeRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.AttractionTypeRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttractionTypeService {

    private final AttractionTypeRepository attractionTypeRepository;

    @Autowired
    public AttractionTypeService(AttractionTypeRepository attractionTypeRepository) {
        this.attractionTypeRepository = attractionTypeRepository;
    }

    public AttractionType create(AttractionTypeRequestData obj) {
        AttractionType attractionType = AttractionType.builder()
                .name(obj.getName())
                .description(obj.getDescription())
                .build();
        return attractionTypeRepository.save(attractionType);
    }

    public AttractionType findById(Long id) {
        Optional<AttractionType> obj = attractionTypeRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + AttractionType.class.getName()));
    }

    public List<AttractionType> findAll() {
        return attractionTypeRepository.findAll();
    }

    public void delete(Long id) {
        findById(id);
        attractionTypeRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return attractionTypeRepository.existsById(id);
    }
}
