package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.moreinfolink.MoreInfoLinkDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.moreinfolink.MoreInfoLinkRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoreInfoLinkService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AttractionService attractionService;

    public MoreInfoLinkService(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    public MoreInfoLinkDTO getMoreInfoLinkFromAttraction(Long attractionId, String link) {
        Attraction attraction = attractionService.findById(attractionId);

        log.info("Obtendo link de mais informações do atrativo de ID: {}", attractionId);

        MoreInfoLink moreInfoLink = attraction.getMoreInfoLinks().stream()
                .filter(obj -> obj.getLink().equals(link))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("MoreInfoLink not found"));

        log.info("Link de mais informações obtido com sucesso.");
        log.debug("Link de mais informações: {}", moreInfoLink);

        return new MoreInfoLinkDTO(
                moreInfoLink.getLink(),
                moreInfoLink.getDescription()
        );
    }

    @Transactional
    public MoreInfoLinkDTO addMoreInfoLinkToAttraction(
            Long attractionId,
            MoreInfoLinkRequestData requestData
    ) {
        Attraction attraction = attractionService.findById(attractionId);

        log.info("Criando link de mais informações para o atrativo de ID: {}", attractionId);
        log.debug("Dados da requisição: {}", requestData);
        log.debug("Atrativo: {}", attraction);

        MoreInfoLink createdMoreInfoLink = new MoreInfoLink(requestData);
        attraction.addMoreInfoLink(createdMoreInfoLink);

        attractionService.save(attraction);

        log.debug("Link de mais informações com sucesso. Attrativo={}", attraction);

        return new MoreInfoLinkDTO(
                createdMoreInfoLink.getLink(),
                createdMoreInfoLink.getDescription()
        );
    }

    @Transactional
    public MoreInfoLinkDTO updateMoreInfoLinkFromAttraction(Long attractionId, String link, MoreInfoLinkRequestData requestData) {
        Attraction attraction = attractionService.findById(attractionId);

        log.debug("Buscando link de mais informações do atrativo de ID={} para atualizar: {}", attractionId, link);

        MoreInfoLink moreInfoLink = attraction.getMoreInfoLinks().stream()
                .filter(obj -> obj.getLink().equals(link))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("MoreInfoLink not found"));

        log.debug("Link de mais informações encontrado: {}", moreInfoLink);

        moreInfoLink.setDescription(requestData.getDescription());
        attractionService.save(attraction);

        log.debug("Link de mais informações atualizado com sucesso.");

        return new MoreInfoLinkDTO(
                moreInfoLink.getLink(),
                moreInfoLink.getDescription()
        );
    }

    public void deleteMoreInfoLinkFromAttraction(Long attractionId, String link) {
        Attraction attraction = attractionService.findById(attractionId);

        log.debug("Buscando link de mais informações do atraticvo de ID={} para deletar: {}", attractionId,  link);

        MoreInfoLink moreInfoLink = attraction.getMoreInfoLinks().stream()
                .filter(obj -> obj.getLink().equals(link))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("MoreInfoLink not found"));

        log.debug("Link de mais informações encontrado: {}", moreInfoLink);

        attraction.getMoreInfoLinks().remove(moreInfoLink);
        attractionService.save(attraction);

        log.debug("Link de mais informações deletado com sucesso.");
    }
}