package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain.Itinerary;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.dto.AddAttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.dto.ItineraryDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.dto.ItineraryRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.service.ItineraryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping("/api/itineraries")
public class ItineraryController {
    private static final Logger log = LoggerFactory.getLogger(ItineraryController.class);

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @PostMapping
    public ResponseEntity<ItineraryDTO> createItinerary(@Valid @RequestBody ItineraryRequestData requestData) {
        log.debug("Creating itinerary with request data: {}", requestData);

        ItineraryDTO itineraryDto = itineraryService.createItinerary(requestData).toDto();
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/itineraries/{id}")
                .buildAndExpand(itineraryDto.getId()).toUri();

        log.info("new itinerary created");
        return ResponseEntity.created(uri).body(itineraryDto);
    }

    @PreAuthorize("@itinerariesAuthorization.isOwnerOfItinerary(#id, authentication) || @itinerariesAuthorization.isItineraryPublic(#id) || hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ItineraryDTO> getItinerary(@PathVariable("id") Long id) {
        log.debug("Retrieving itinerary with id: {}", id);

        ItineraryDTO itineraryDto = itineraryService.findById(id).toDto();

        log.info("Itinerary retrieved successfully");
        return ResponseEntity.ok(itineraryDto);
    }

    @PreAuthorize("@itinerariesAuthorization.isOwnerOfItinerary(#id, authentication)")
    @PostMapping("/{id}/items")
    public ResponseEntity<Void> addItemToItinerary(@PathVariable("id") Long id, @Valid @RequestBody AddAttractionRequestData requestData) {
        log.debug("Adding item to itinerary with id: {}", id);

        itineraryService.addItemToItinerary(id, requestData);

        log.info("Item added to itinerary successfully");

        return ResponseEntity.ok(null);
    }

    @PreAuthorize("@itinerariesAuthorization.isOwnerOfItinerary(#id, authentication)")
    @DeleteMapping("/{id}/items/{attractionId}")
    public ResponseEntity<Void> removeItemFromItinerary(@PathVariable("id") Long id, @PathVariable("attractionId") Long attractionId) {
        log.debug("Deleting item with attractionId: {} from itinerary with id: {}", attractionId, id);

        itineraryService.removeAttraction(id, attractionId);

        log.info("Item deleted from itinerary successfully");

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@itinerariesAuthorization.isOwnerOfItinerary(#id, authentication) || hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItinerary(@PathVariable("id") Long id) {
        log.debug("Deleting itinerary with id: {}", id);

        itineraryService.deleteItinerary(id);

        log.info("Itinerary deleted successfully");

        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<Page<ItineraryDTO>> getPublicItineraries(Pageable pageable) {
        log.debug("Retrieving public itineraries with pageable: {}", pageable);

        Page<ItineraryDTO> itineraries = itineraryService.findAllPublicItineraries(pageable).map(Itinerary::toDto);

        log.info("Itineraries retrieved successfully");
        log.debug("Retrieved {} itineraries", itineraries.getTotalElements());
        return ResponseEntity.ok(itineraries);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<ItineraryDTO>> getAllItineraries(Pageable pageable) {
        log.debug("Retrieving all itineraries with pageable: {}", pageable);

        Page<ItineraryDTO> itineraries = itineraryService.findAllItineraries(pageable).map(Itinerary::toDto);

        log.info("Itineraries retrieved successfully");
        log.debug("Retrieved {} itineraries", itineraries.getTotalElements());
        return ResponseEntity.ok(itineraries);
    }

}
