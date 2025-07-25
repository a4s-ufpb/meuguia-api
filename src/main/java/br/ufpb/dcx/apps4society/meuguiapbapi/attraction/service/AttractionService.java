package br.ufpb.dcx.apps4society.meuguiapbapi.attraction.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.repository.AttractionRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.specification.AttractionSpecification;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.repository.AttractionTypeService;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.domain.City;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.service.CityService;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ResourceAlreadyExistsException;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.service.TourismSegmentationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AttractionService {
    private static final Logger log = LoggerFactory.getLogger(AttractionService.class);

    private final TourismSegmentationService tourismSegmentationService;
    private final AttractionTypeService attractionTypeService;
    private final AttractionRepository attractionRepository;
    private final CityService cityService;

    @Autowired
    public AttractionService(AttractionTypeService attractionTypeService,
                             TourismSegmentationService tourismSegmentationService,
                             AttractionRepository attractionRepository,
                             CityService cityService) {
        this.attractionTypeService = attractionTypeService;
        this.tourismSegmentationService = tourismSegmentationService;
        this.attractionRepository = attractionRepository;
        this.cityService = cityService;
    }

    @Transactional
    public Attraction create(AttractionRequestData obj) {
        verifyExistence(obj);
        validateFields(obj);

        List<TourismSegmentation> segmentations = obj.getSegmentations().stream().map(tourismSegmentationService::findById).toList();
        List<MoreInfoLink> moreInfoLinks = obj.getMoreInfoLinks().stream().map(MoreInfoLink::new).toList();
        AttractionType attractionType = attractionTypeService.findById(obj.getAttractionType());
        City city = cityService.findById(obj.getCityId());

        Attraction attraction = Attraction.builder()
                .name(obj.getName())
                .description(obj.getDescription())
                .mapLink(obj.getMapLink())
                .city(city)
                .imageLink(obj.getImageLink())
                .segmentations(segmentations)
                .attractionType(attractionType)
                .moreInfoLinks(moreInfoLinks)
                .build();

        return attractionRepository.save(attraction);
    }

    public void verifyExistence(AttractionRequestData obj) {
        if (attractionRepository.findByNameAndCityId(obj.getName(), obj.getCityId()).isPresent()) {
            throw new ResourceAlreadyExistsException("Atração com nome '" + obj.getName() + "' já cadastrada para a cidade '" + obj.getCityId() + "'.");
        }
    }

    private void validateFields(AttractionRequestData obj) {
        if (!this.attractionTypeService.existsById(obj.getAttractionType())) {
            throw new ObjectNotFoundException("Tipo de atração não encontrado! Id: " + obj.getAttractionType());
        }

        if (!this.cityService.existsById(obj.getCityId())) {
            throw new ObjectNotFoundException("Cidade não encontrada! Id: " + obj.getCityId());
        }

        for (Long segmentationId : obj.getSegmentations()) {
            if (!this.tourismSegmentationService.existsById(segmentationId)) {
                throw new ObjectNotFoundException("Segmentação não encontrada! Id: " + segmentationId);
            }
        }
    }

    public Attraction findById(Long id) {
        log.debug("Buscando atrativo por id: {}", id);

        Optional<Attraction> obj = attractionRepository.findById(id);
        Attraction attraction = obj.orElseThrow(() ->
                new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Attraction.class.getName()));

        log.debug("Atrativo encontrado com sucesso");
        log.debug("Atrativo encontrado: {}", attraction);

        return attraction;
    }

    public Page<Attraction> findAll(Pageable pageable) {
        return attractionRepository.findAll(pageable);
    }


    public Page<Attraction> search(Pageable pageable, AttractionSpecification specification) {
        return attractionRepository.findAll(specification, pageable);
    }

    public void delete(Long id) {
        findById(id);
        attractionRepository.deleteById(id);
    }

    public Attraction update(Long id, AttractionRequestData obj) {
        validateFields(obj);

        Attraction attraction = findById(id);
        attraction.setName(obj.getName());
        attraction.setDescription(obj.getDescription());
        attraction.setMapLink(obj.getMapLink());
        attraction.setImageLink(obj.getImageLink());
        return attractionRepository.save(attraction);
    }

    public Attraction save(Attraction attraction) {
        return attractionRepository.save(attraction);
    }

}
