package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
}
