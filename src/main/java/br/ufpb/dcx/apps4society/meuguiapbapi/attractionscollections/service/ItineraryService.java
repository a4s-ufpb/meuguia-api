package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.service.AttractionService;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain.Itinerary;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.dto.AddAttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.dto.ItineraryRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.repository.ItineraryRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ItineraryService {
    private static final Logger log = LoggerFactory.getLogger(ItineraryService.class);
    private final ItineraryRepository itineraryRepository;
    private final AttractionService attractionService;

    public ItineraryService(ItineraryRepository itineraryRepository, AttractionService attractionService) {
        this.itineraryRepository = itineraryRepository;
        this.attractionService = attractionService;
    }

    public Itinerary createItinerary(ItineraryRequestData itineraryRequestData) {
        log.debug("Creating itinerary with request data: {}", itineraryRequestData);

        Itinerary itinerary = new Itinerary(
                itineraryRequestData.getName(),
                itineraryRequestData.getDescription(),
                true,
                itineraryRequestData.isPublic(),
                itineraryRequestData.isRanked()
        );

        itineraryRequestData.getItems().forEach(itineraryItemId -> {
            var attraction = attractionService.findById(itineraryItemId);
            itinerary.addAttraction(attraction);
        });
        log.debug("Itinerary created: {}", itinerary);
        return itineraryRepository.save(itinerary);
    }

    public Itinerary findById(Long id) {
        log.debug("Finding itinerary by id: {}", id);
        return itineraryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Itinerary not found with id: " + id));
    }

    public void addItemToItinerary(Long id, AddAttractionRequestData requestData) {
        log.debug("Adding item to itinerary with id: {}, request data: {}", id, requestData);

        Itinerary itinerary = findById(id);
        var attraction = attractionService.findById(requestData.getAttractionId());

        if (itinerary.hasAttraction(attraction)) {
            log.warn("Attraction with id {} already exists in itinerary with id {}", requestData.getAttractionId(), id);
            return;
        }

        itinerary.addAttraction(attraction);

        log.debug("Item added to itinerary: {}", itinerary);
        itineraryRepository.save(itinerary);
    }

    public void deleteItinerary(Long id) {
        itineraryRepository.deleteById(id);
    }

    public void removeAttraction(Long id, Long attractionId) {
        log.debug("Removing attraction with id: {} from itinerary with id: {}", attractionId, id);

        Itinerary itinerary = findById(id);
        var attraction = attractionService.findById(attractionId);

        if (!itinerary.hasAttraction(attraction)) {
            log.warn("Attraction with id {} does not exist in itinerary with id {}", attractionId, id);
            throw new ObjectNotFoundException("Attraction with id " + attractionId + " does not exist in itinerary with id " + id);
        }

        itinerary.removeAttraction(attraction);
        itineraryRepository.save(itinerary);

        log.debug("Attraction removed from itinerary: {}", itinerary);
    }
}
