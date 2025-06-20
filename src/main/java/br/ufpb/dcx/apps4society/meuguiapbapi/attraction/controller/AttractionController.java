package br.ufpb.dcx.apps4society.meuguiapbapi.attraction.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.service.AttractionService;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.specification.AttractionSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/attractions", produces = {"application/json"})
@Tag(name = "Attractions", description = "Endpoints para gerenciar atrativos")
public class AttractionController {
    private final Logger log = LoggerFactory.getLogger(AttractionController.class);

    private final AttractionService attractionService;

    @Autowired
    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @Operation(summary = "Buscar atrativo por id", description = "Busca um atrativo passando o seu id",
            tags = {"Attractions"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = AttractionDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<Attraction> findById(@PathVariable Long id) {
        log.info("Buscando atrativo pelo ID: {}", id);
        Attraction obj = attractionService.findById(id);
        log.info("Atrativo encontrado pelo ID: {}", obj);

        return ResponseEntity.ok().body(obj);
    }

    @Operation(summary = "Cadastro de atrativo",
            description = "Cadastra um atrativo",
            tags = {"Attractions"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = Attraction.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PostMapping()
    public ResponseEntity<AttractionDTO> create(@RequestBody @Valid AttractionRequestData obj) {
        log.info("Criando novo atrativo: {}", obj.getName());
        log.debug("Atrativo: {}", obj);

        Attraction createdAttraction = attractionService.create(obj);
        AttractionDTO dto = new AttractionDTO(createdAttraction);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/attractions/{id}")
                .buildAndExpand(createdAttraction.getId()).toUri();

        log.info("Atrativo criado com sucesso: {}", createdAttraction.getName());
        log.debug("Atrativo criado: {}", createdAttraction);

        return ResponseEntity.created(uri).body(dto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deleta um atrativo",
            description = "Deleta um Atrativo passando o seu id",
            tags = {"Attractions"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deletando atrativo com ID: {}", id);
        attractionService.delete(id);
        log.info("Atrativo deletado com sucesso");
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listagem de todos os atrativos", description = "Lista todos os atrativos",
            tags = {"Attractions"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AttractionDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @GetMapping
    public ResponseEntity<Page<AttractionDTO>> findAll(Pageable pageable) {
        log.info("Buscando todos os atrativos");

        Page<Attraction> attractionPage = attractionService.findAll(pageable);
        Page<AttractionDTO> attractionDTOPage = attractionPage.map(AttractionDTO::new);

        log.info("Atrativos totais encontrados: {}", attractionPage.getTotalElements());
        log.info("Returning page {} with size {}", attractionPage.getPageable().getPageNumber(), attractionPage.getSize());
        return ResponseEntity.ok().body(attractionDTOPage);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AttractionDTO>> search(Pageable pageable, AttractionSpecification specification) {
        log.info("Buscando todos os atrativos");
        log.debug("Especificação de busca: {}", specification);

        Page<AttractionDTO> attractionPage = attractionService.search(pageable, specification).map(AttractionDTO::new);

        log.info("Atrativos totais encontrados: {}", attractionPage.getTotalElements());
        log.info("Returning page {} with size {}", attractionPage.getPageable().getPageNumber(), attractionPage.getSize());
        return ResponseEntity.ok().body(attractionPage);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AttractionDTO> update(@PathVariable Long id, @RequestBody @Valid AttractionRequestData objDto) {
        log.info("Atualizando atrativo com ID: {}", id);

        Attraction updatedAttraction = attractionService.update(id, objDto);
        AttractionDTO updatedAttractionDTO = new AttractionDTO(updatedAttraction);

        log.info("Atrativo atualizado com sucesso: {}", updatedAttraction);

        return ResponseEntity.ok().body(updatedAttractionDTO);
    }
}
