package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain.ItineraryItem;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain.ItineraryItemId;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.repository.ItineraryItemRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ItineraryItemService {
    private final ItineraryItemRepository itineraryItemRepository;

    public ItineraryItemService(ItineraryItemRepository itineraryItemRepository) {
        this.itineraryItemRepository = itineraryItemRepository;
    }

    public ItineraryItem save(ItineraryItem itineraryItem) {
        return itineraryItemRepository.save(itineraryItem);
    }

    public ItineraryItem findItineraryItemById(ItineraryItemId id) {
        return itineraryItemRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Itinerary item not found with id: " + id));
    }

    public void deleteItineraryItem(ItineraryItemId id) {
        if (!itineraryItemRepository.existsById(id)) {
            throw new ObjectNotFoundException("Itinerary item not found with id: " + id);
        }
        itineraryItemRepository.deleteById(id);
    }

}
