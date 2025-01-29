package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ResourceAlreadyExistsException;
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
    private final AttractionRepository attractionRepository;

    @Autowired
    public AttractionService(MoreInfoLinkService moreInfoLinkService,
                             AttractionTypeService attractionTypeService,
                             TourismSegmentationService tourismSegmentationService,
                             AttractionRepository turistAttractionRepository, AttractionRepository attractionRepository) {
        this.moreInfoLinkService = moreInfoLinkService;
        this.attractionTypeService = attractionTypeService;
        this.tourismSegmentationService = tourismSegmentationService;
        this.turistAttractionRepository = turistAttractionRepository;
        this.attractionRepository = attractionRepository;
    }

    public Attraction create(AttractionRequestData obj) {
        verifyExistence(obj);
        validateFields(obj);

        var segmentations = obj.getSegmentations().stream().map(tourismSegmentationService::findById).toList();
        var attractionType = attractionTypeService.findById(obj.getAttractionType());
        var moreInfoLinks = obj.getMoreInfoLinks().stream().map(moreInfoLinkService::findById).toList();

        Attraction attraction = Attraction.builder()
                .name(obj.getName())
                .description(obj.getDescription())
                .mapLink(obj.getMapLink())
                .city(obj.getCity())
                .state(obj.getState())
                .imageLink(obj.getImageLink())
                .infoSource(obj.getInfoSource())
                .segmentations(segmentations)
                .attractionType(attractionType)
                .moreInfoLinkList(moreInfoLinks)
                .build();

        return turistAttractionRepository.save(attraction);
    }

    public void verifyExistence(AttractionRequestData obj) {
        if (attractionRepository.findByNameAndCity(obj.getName(), obj.getCity()).isPresent()) {
            throw new ResourceAlreadyExistsException("Atração com nome '" + obj.getName() + "' já cadastrada para a cidade '" + obj.getCity() + "'.");
        }
    }

    private void validateFields(AttractionRequestData obj) {
        if (!this.attractionTypeService.existsById(obj.getAttractionType())) {
            throw new ObjectNotFoundException("Tipo de atração não encontrado! Id: " + obj.getAttractionType());
        }

        for (Long segmentationId: obj.getSegmentations()) {
            if (!this.tourismSegmentationService.existsById(segmentationId)) {
                throw new ObjectNotFoundException("Segmentação não encontrada! Id: " + segmentationId);
            }
        }

        for (Long moreInfoLinkId: obj.getMoreInfoLinks()) {
            if (!this.moreInfoLinkService.existsById(moreInfoLinkId)) {
                throw new ObjectNotFoundException("Link de mais informações não encontrado! Id: " + moreInfoLinkId);
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
