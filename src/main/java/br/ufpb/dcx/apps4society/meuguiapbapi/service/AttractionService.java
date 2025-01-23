package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.AttractionRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttractionService {

    private final AttractionRepository turistAttractionRepository;
    private final TourismSegmentationService tourismSegmentationService;
    private final AttractionTypeService attractionTypeService;
    private final MoreInfoLinkService moreInfoLinkService;

    @Autowired
    public AttractionService(MoreInfoLinkService moreInfoLinkService,
                             AttractionTypeService attractionTypeService,
                             TourismSegmentationService tourismSegmentationService,
                             AttractionRepository turistAttractionRepository) {
        this.moreInfoLinkService = moreInfoLinkService;
        this.attractionTypeService = attractionTypeService;
        this.tourismSegmentationService = tourismSegmentationService;
        this.turistAttractionRepository = turistAttractionRepository;
    }

    public Attraction create(AttractionRequestData obj) {
        validateFields(obj);

        Attraction attraction = Attraction.builder()
                .name(obj.getName())
                .description(obj.getDescription())
                .mapLink(obj.getMapLink())
                .city(obj.getCity())
                .state(obj.getState())
                .imageLink(obj.getImageLink())
                .infoSource(obj.getInfoSource())
                .segmentations(obj.getSegmentations())
                .attractionType(obj.getAttractionType())
                .moreInfoLinkList(obj.getMoreInfoLinks())
                .build();

        return turistAttractionRepository.save(attraction);
    }

    private void validateFields(AttractionRequestData obj) {
        if (!this.attractionTypeService.existsById(obj.getAttractionType().getId())) {
            throw new ObjectNotFoundException("Tipo de atração não encontrado! Id: " + obj.getAttractionType().getId());
        }

        for (TourismSegmentation segmentation : obj.getSegmentations()) {
            if (!this.tourismSegmentationService.existsById(segmentation.getId())) {
                throw new ObjectNotFoundException("Segmentação não encontrada! Id: " + segmentation.getId());
            }
        }

        for (MoreInfoLink moreInfoLink : obj.getMoreInfoLinks()) {
            if (!this.moreInfoLinkService.existsById(moreInfoLink.getId())) {
                throw new ObjectNotFoundException("Link de mais informações não encontrado! Id: " + moreInfoLink.getId());
            }
        }
    }

    public Attraction findById(Long id) {
        Optional<Attraction> obj = turistAttractionRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Attraction.class.getName()));
    }

    public List<Attraction> findAll() {
        return turistAttractionRepository.findAll();
    }

    public List<Attraction> findByName(String name) {
        return turistAttractionRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Attraction> findByCity(String city) {
        return turistAttractionRepository.findAllByCity(city);
    }

    public List<Attraction> findBySegmentation(String segmentationName) {
        if (!this.tourismSegmentationService.existsByName(segmentationName)) {
            throw new ObjectNotFoundException("Segmentação com nome '" + segmentationName + "' não encontrada!");
        }
        return turistAttractionRepository.findAllBySegmentationName(segmentationName);
    }

    public List<Attraction> findByType(String attractionTypes) {
        return turistAttractionRepository.findAllByType(attractionTypes);
    }

    public void delete(Long id) {
        findById(id);
        turistAttractionRepository.deleteById(id);
    }

    public Attraction update(Long id, AttractionRequestData obj) {
        validateFields(obj);

        Attraction attraction = findById(id);
        attraction.setName(obj.getName());
        attraction.setDescription(obj.getDescription());
        attraction.setMapLink(obj.getMapLink());
        attraction.setCity(obj.getCity());
        attraction.setState(obj.getState());
        attraction.setImageLink(obj.getImageLink());
        attraction.setInfoSource(obj.getInfoSource());
        return turistAttractionRepository.save(attraction);
    }
}
