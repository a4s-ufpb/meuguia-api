package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.MoreInfoLinkRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.TouristAttractionDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.service.MoreInfoLinkService;
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

@RestController
@RequestMapping(value = "/api/more-info", produces = {"application/json"})
@Tag(name = "More Info Link", description = "Endpoints para gerenciar os links para mais informações dos atrativos")
public class MoreInfoLinkController {
    private final Logger log = LoggerFactory.getLogger(MoreInfoLinkController.class);

    private final MoreInfoLinkService moreInfoLinkService;

    @Autowired
    public MoreInfoLinkController(MoreInfoLinkService moreInfoLinkService) {
        this.moreInfoLinkService = moreInfoLinkService;
    }

    @Operation(summary = "Buscar um link do atrativo por id", description = "Busca um link do atrativo por id",
            tags = {"More Info Link"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TouristAttractionDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<MoreInfoLink> findById(@PathVariable Long id) {
        log.info("Buscando link de mais informações com ID: {}", id);
        MoreInfoLink obj = moreInfoLinkService.findById(id);
        log.info("Link encontrado: {}", obj);
        return ResponseEntity.ok().body(obj);
    }

    @Operation(summary = "Listagem de todos os links para mais informações dos atrativos",
            description = "Lista todos os links para mais informações dos atrativos",
            tags = {"More Info Link"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = TouristAttractionDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @GetMapping
    public List<MoreInfoLink> findAll() {
        log.info("Buscando todos os links de mais informações");
        List<MoreInfoLink> list = moreInfoLinkService.findAll();
        log.info("Total de links encontrados: {}", list.size());
        return ResponseEntity.ok().body(list).getBody();
    }

    @Operation(summary = "Cadastro de links para mais informações de atrativos",
            description = "Cadastra os links para mais informações do atrativo",
            tags = {"More Info Link"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = TouristAttractionDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PostMapping
    public ResponseEntity<MoreInfoLink> create(@RequestBody @Valid MoreInfoLinkRequestData obj) {
        log.info("Cadastrando novo link de mais informações: {}", obj);
        MoreInfoLink newObj = moreInfoLinkService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/more-info/{id}")
                .buildAndExpand(newObj.getId()).toUri();
        log.info("Link de mais informações criado com sucesso: {}", newObj);
        return ResponseEntity.created(uri).body(newObj);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deleta um link de mais informações de atrativo",
            description = "Deleta um link de mais informações de Atrativo passando o seu id",
            tags = {"More Info Link"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deletando link de mais informações com ID: {}", id);
        moreInfoLinkService.delete(id);
        log.info("Link de mais informações deletado com sucesso");
        return ResponseEntity.noContent().build();
    }
}
