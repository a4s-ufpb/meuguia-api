package br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.dto.AttractionTypeDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.dto.AttractionTypeRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.repository.AttractionTypeService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/types", produces = {"application/json"})
@Tag(name = "Attractions Types", description = "Endpoints para gerenciar tipos de atrativos")
public class AttractionTypeController {

    private static final Logger logger = LoggerFactory.getLogger(AttractionTypeController.class);

    private final AttractionTypeService attractionTypeService;

    @Autowired
    public AttractionTypeController(AttractionTypeService attractionTypeService) {
        this.attractionTypeService = attractionTypeService;
    }

    @Operation(summary = "Cadastro de tipos de atrativos",
            description = "Cadastra um tipo de atrativo",
            tags = {"Attractions Types"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = AttractionDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PostMapping
    public ResponseEntity<AttractionType> create(@RequestBody @Valid AttractionTypeRequestData obj) {
        logger.info("Criando novo tipo de atrativo: {}", obj);
        AttractionType newObj = attractionTypeService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/types/{id}")
                .buildAndExpand(newObj.getId()).toUri();
        logger.info("Tipo de atrativo criado com sucesso: {}", newObj);
        return ResponseEntity.created(uri).body(newObj);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deleta um tipo de atrativo",
            description = "Deleta um tipo de Atrativo passando o seu id",
            tags = {"Attractions Types"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deletando tipo de atrativo com ID: {}", id);
        attractionTypeService.delete(id);
        logger.info("Tipo de atrativo deletado com sucesso");
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<AttractionType>> search(@RequestParam(value = "name", required = false, defaultValue = "") String name) {
        logger.info("Buscando tipo de atrativo pelo link: {}", name);
        List<AttractionType> list = attractionTypeService.search(name);
        logger.info("Tipo de atrativo encontrado: {}", list.size());
        return ResponseEntity.ok().body(list);

    }

    @Operation(summary = "Listagem de todos os tipos de atrativos", description = "Lista todos os tipos de atrativos",
            tags = {"Attractions Types"},
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
    public ResponseEntity<Page<AttractionTypeDTO>> findAll(Pageable pageable) {
        logger.info("Buscando todos os tipos de atrativos");
        Page<AttractionTypeDTO> typesDto = attractionTypeService.findAll(pageable).map(AttractionTypeDTO::new);
        logger.info("Tipos de atrativos encontrados: {}", typesDto.getTotalElements());
        return ResponseEntity.ok().body(typesDto);
    }
}
