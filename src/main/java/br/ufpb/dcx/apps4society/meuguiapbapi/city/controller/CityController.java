package br.ufpb.dcx.apps4society.meuguiapbapi.city.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.city.domain.City;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.dto.CityDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.dto.CityRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.service.CityService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/cities", produces = MediaType.APPLICATION_JSON_VALUE)
public class CityController {

    private static final Logger log = LoggerFactory.getLogger(CityController.class);
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CityDTO> createCity(
            @Valid @RequestBody CityRequestData cityRequestData
    ) {
        log.debug("createCity called with {}", cityRequestData);

        CityDTO createdCity = cityService.createCity(cityRequestData).toDto();
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/cities/{id}")
                .buildAndExpand(createdCity.getId())
                .toUri();

        log.debug("city created");
        return ResponseEntity.created(uri).body(createdCity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CityDTO> updateCity(
            @PathVariable Long id,
            @RequestBody CityRequestData cityRequestData
    ) {
        log.debug("updateCity called with {}", cityRequestData);

        CityDTO updatedCity = cityService.updateCity(id, cityRequestData).toDto();

        log.debug("city updated");
        return ResponseEntity.ok(updatedCity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCity(
            @PathVariable Long id
    ) {
        log.debug("deleteCity called with id {}", id);

        cityService.deleteCity(id);

        log.debug("city deleted");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCity(
            @PathVariable Long id
    ) {
        log.debug("getCity called with id {}", id);

        CityDTO city = cityService.findById(id).toDto();

        log.debug("city founded");
        return ResponseEntity.ok(city);
    }

    @GetMapping
    public ResponseEntity<Page<CityDTO>> getCities(
            Pageable pageable
    ) {
        log.debug("getCities called");

        Page<CityDTO> cities = cityService.findAll(pageable).map(City::toDto);

        log.debug("{} cities founded", cities.getTotalElements());
        return ResponseEntity.ok(cities);
    }
}
