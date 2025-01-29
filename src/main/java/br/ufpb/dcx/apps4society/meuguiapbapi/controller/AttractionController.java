package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.service.AttractionService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
                            content = @Content(schema = @Schema(implementation = AttractionRequestData.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PostMapping()
    public ResponseEntity<Attraction> create(@RequestBody @Valid AttractionRequestData obj) {
        log.info("Criando novo atrativo: {}", obj);
        Attraction newObj = attractionService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/attractions/{id}")
                .buildAndExpand(newObj.getId()).toUri();
        log.info("Atrativo criado com sucesso: {}", newObj);
        return ResponseEntity.created(uri).body(newObj);
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
    public ResponseEntity<List<AttractionDTO>> findAll() {
        log.info("Buscando todos os atrativos");
        List<Attraction> list = attractionService.findAll();
        List<AttractionDTO> listDTO = list.stream().map(AttractionDTO::new).collect(Collectors.toList());
        log.info("Atrativos encontrados: {}", listDTO.size());
        return ResponseEntity.ok().body(listDTO);
    }

    @Operation(summary = "Buscar atrativo por nome", description = "Busca um atrativo pelo nome",
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
    @GetMapping(value = "/byName")
    public ResponseEntity<List<Attraction>> findByName(@RequestParam String name) {
        log.info("Buscando atrativo pelo nome: {}", name);
        List<Attraction> list = attractionService.findByName(name);
        log.info("Atrativo encontrado pelo nome: {}", list.size());
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "Buscar atrativo por cidade", description = "Busca um atrativo por cidade",
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
    @GetMapping(value = "/byCity")
    public ResponseEntity<List<Attraction>> findByCity(@RequestParam String city) {
        log.info("Buscando atrativo pela cidade: {}", city);
        List<Attraction> list = attractionService.findByCity(city);
        log.info("Atrativos encontrados pela cidade: {}", list.size());
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "Buscar atrativo por segmentação", description = "Busca um atrativo pela segmentação",
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
    @GetMapping(value = "/bySegmentations")
    public ResponseEntity<List<Attraction>> findBySegmentations(@RequestParam String segmentation) {
        log.info("Buscando atrativo pela segmentação: {}", segmentation);
        List<Attraction> list = attractionService.findBySegmentation(segmentation);
        log.info("Atrativos encontrados pela segmentação: {}", list.size());
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "Buscar atrativo por tipo", description = "Busca um atrativo pelo tipo",
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
    @GetMapping(value = "/byType")
    public ResponseEntity<List<Attraction>> findByType(@RequestParam String attractionType) {
        log.info("Buscando atrativo pelo tipo: {}", attractionType);
        List<Attraction> list = attractionService.findByType(attractionType);
        log.info("Atrativos encontrados pelo tipo: {}", list.size());
        return ResponseEntity.ok().body(list);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Attraction> update(@PathVariable Long id, @RequestBody @Valid AttractionRequestData objDto) {
        log.info("Atualizando atrativo com ID: {}", id);
        Attraction newObj = attractionService.update(id, objDto);
        log.info("Atrativo atualizado com sucesso: {}", newObj);
        return ResponseEntity.ok().body(newObj);
    }
}
