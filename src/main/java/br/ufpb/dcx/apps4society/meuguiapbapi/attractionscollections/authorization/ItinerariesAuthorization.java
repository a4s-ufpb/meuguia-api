package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.authorization;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain.Itinerary;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.repository.ItineraryRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("itinerariesAuthorization")
public class ItinerariesAuthorization {

    private ItineraryRepository itineraryRepository;

    public ItinerariesAuthorization(ItineraryRepository itineraryRepository) {
        this.itineraryRepository = itineraryRepository;
    }

    public boolean isOwnerOfItinerary(Long itineraryId, Authentication authentication) {
        return itineraryRepository.findById(itineraryId)
                .map(itinerary -> itinerary.getOwner().getUsername().equals(authentication.getName()))
                .orElse(false);
    }

    public boolean isItineraryPublic(Long itineraryId) {
        return itineraryRepository.findById(itineraryId)
                .map(Itinerary::isPublic)
                .orElse(false);
    }
}
