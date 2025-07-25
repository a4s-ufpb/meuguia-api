package br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain.ItineraryItem;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionscollections.domain.ItineraryItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryItemRepository extends JpaRepository<ItineraryItem, ItineraryItemId> {

}
