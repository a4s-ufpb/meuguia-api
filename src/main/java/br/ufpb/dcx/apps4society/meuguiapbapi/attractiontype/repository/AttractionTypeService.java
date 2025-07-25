package br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.dto.AttractionTypeRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<AttractionType> findAll(Pageable pageable) {
        return attractionTypeRepository.findAll(pageable);
    }

    public List<AttractionType> search(String name) {
        return attractionTypeRepository.findByNameContainingIgnoreCase(name);
    }

    public void delete(Long id) {
        findById(id);
        attractionTypeRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return attractionTypeRepository.existsById(id);
    }
}
