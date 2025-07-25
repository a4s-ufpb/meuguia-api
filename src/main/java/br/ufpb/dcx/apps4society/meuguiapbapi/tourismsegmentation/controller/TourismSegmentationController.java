package br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.service.TourismSegmentationService;
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
import java.util.List;

@RestController
@RequestMapping(value = "/api/segmentations", produces = {"application/json"})
@Tag(name = "Tourism Segmentation", description = "Endpoints para gerenciar as segmentações turísticas dos atrativos")
public class TourismSegmentationController {
    private final Logger log = LoggerFactory.getLogger(TourismSegmentationController.class);

    private final TourismSegmentationService tourismSegmentationService;

    @Autowired
    public TourismSegmentationController(TourismSegmentationService tourismSegmentationService) {
        this.tourismSegmentationService = tourismSegmentationService;
    }

    @Operation(summary = "Buscar a segmentação turística do atrativo por id", description = "Busca uma segmentação turística do atrativo por id",
            tags = {"Tourism Segmentation"},
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
    public ResponseEntity<TourismSegmentation> findById(@PathVariable Long id) {
        log.info("Buscando segmentação turística com ID: {}", id);
        TourismSegmentation obj = tourismSegmentationService.findById(id);
        log.info("Segmentação turística encontrada: {}", obj);
        return ResponseEntity.ok().body(obj);
    }

    @Operation(summary = "Listagem de todas as segmentações turísticas do atrativo",
            description = "Lista todas as segmentações turísticas do atrativo",
            tags = {"Tourism Segmentation"},
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
    public ResponseEntity<Page<TourismSegmentationDTO>> findAll(Pageable pageable) {
        log.info("Buscando todas as segmentações turísticas");
        Page<TourismSegmentationDTO> list = tourismSegmentationService.findAll(pageable).map(TourismSegmentationDTO::new);
        log.info("Total de segmentações encontradas: {}", list.getTotalElements());
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "Cadastro de segmentações turísticas",
            description = "Cadastra as segmentações turísticas dos atrativos",
            tags = {"Tourism Segmentation"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = AttractionDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PostMapping
    public ResponseEntity<TourismSegmentation> create(@RequestBody @Valid TourismSegmentationRequestData obj) {
        log.info("Cadastrando nova segmentação turística: {}", obj);
        TourismSegmentation newObj = tourismSegmentationService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/segmentations/{id}")
                .buildAndExpand(newObj.getId()).toUri();
        log.info("Segmentação turística criada com sucesso: {}", newObj);
        return ResponseEntity.created(uri).body(newObj);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deleta uma segmentação turística passando o seu id",
            description = "Deleta uma segmentação turística passando o seu id",
            tags = {"Tourism Segmentation"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deletando segmentação turística com ID: {}", id);
        tourismSegmentationService.delete(id);
        log.info("Segmentação turística deletada com sucesso");
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<TourismSegmentation>> search(@RequestParam(value = "name", required = false) String name) {
        log.info("Buscando segmentações turísticas por nome: {}", name);
        List<TourismSegmentation> list = tourismSegmentationService.searchByName(name);
        log.info("Total de segmentações encontradas: {}", list.size());
        return ResponseEntity.ok().body(list);
    }
}
