package br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.service.MoreInfoLinkService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(path = "/api/attractions/{attractionId}/more-info-links", produces = MediaType.APPLICATION_JSON_VALUE)
public class MoreInfoLinkController {
    private final Logger log = LoggerFactory.getLogger(MoreInfoLinkController.class);
    private final MoreInfoLinkService moreInfoLinkService;

    @Autowired
    public MoreInfoLinkController(MoreInfoLinkService moreInfoLinkService) {
        this.moreInfoLinkService = moreInfoLinkService;
    }

    @PostMapping
    public ResponseEntity<MoreInfoLinkDTO> addMoreInfoLinkToAttraction(
            @PathVariable Long attractionId,
            @RequestBody @Valid MoreInfoLinkRequestData requestData
    ) {
        log.debug("Recebendo requisição para adicionar link de mais informações a atração de ID: {}", attractionId);

        MoreInfoLinkDTO createdMoreInfoLink = moreInfoLinkService.addMoreInfoLinkToAttraction(attractionId, requestData);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/api/attractions/{attractionId}/more-info-links?link={link}")
                .buildAndExpand(attractionId, createdMoreInfoLink.getLink())
                .toUri();

        log.debug("Link de mais informações adicionado com sucesso a atração de ID: {}", attractionId);

        return ResponseEntity.created(uri).body(createdMoreInfoLink);
    }

    @GetMapping
    public ResponseEntity<MoreInfoLinkDTO> getMoreInfoLinkFromAttraction(
            @PathVariable Long attractionId,
            @RequestParam(name = "link") String encodedLink
    ) {
        log.debug("Recebendo requisição para obter link de mais informações da atração de ID: {}", attractionId);

        String link = URLDecoder.decode(encodedLink, StandardCharsets.UTF_8);
        MoreInfoLinkDTO foundMoreInfoLink = moreInfoLinkService.getMoreInfoLinkFromAttraction(attractionId, link);

        log.debug("Link de mais informações encontrado com sucesso a atração de ID: {}", attractionId);

        return ResponseEntity.ok().body(foundMoreInfoLink);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MoreInfoLinkDTO> updateMoreInfoLinkFromAttraction(
            @PathVariable Long attractionId,
            @RequestParam(name = "link") String encodedLink,
            @RequestBody @Valid MoreInfoLinkRequestData requestData
    ) {
        log.debug("Recebendo requisição para atualizar link de mais informações da atração de ID: {}", attractionId);

        String link = URLDecoder.decode(encodedLink, StandardCharsets.UTF_8);
        MoreInfoLinkDTO updatedMoreInfoLink = moreInfoLinkService.updateMoreInfoLinkFromAttraction(attractionId, link, requestData);

        log.debug("Link de mais informações atualizado com sucesso a atração de ID: {}", attractionId);

        return ResponseEntity.ok().body(updatedMoreInfoLink);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMoreInfoLinkFromAttraction(
            @PathVariable Long attractionId,
            @RequestParam(name = "link") String encodedLink
    ) {
        log.debug("Recebendo requisição para deletar link de mais informações da atração de ID: {}", attractionId);

        String link = URLDecoder.decode(encodedLink, StandardCharsets.UTF_8);
        moreInfoLinkService.deleteMoreInfoLinkFromAttraction(attractionId, link);

        log.debug("Link de mais informações deletado com sucesso no atrativo de ID: {}", attractionId);

        return ResponseEntity.noContent().build();
    }
}
