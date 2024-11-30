package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.MoreInfoLinkRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.MoreInfoLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MoreInfoLinkService {

    private final MoreInfoLinkRepository moreInfoLinkRepository;

    @Autowired
    public MoreInfoLinkService(MoreInfoLinkRepository moreInfoLinkRepository) {
        this.moreInfoLinkRepository = moreInfoLinkRepository;
    }

    public MoreInfoLink findById(Long id) {
        Optional<MoreInfoLink> obj = moreInfoLinkRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! id: " + id + ", Tipo " + MoreInfoLink.class.getName()));

    }

    public List<MoreInfoLink> findAll() {
        return moreInfoLinkRepository.findAll();
    }

    public MoreInfoLink create(MoreInfoLinkRequestData obj) {
        MoreInfoLink moreInfoLink = MoreInfoLink.builder()
                .link(obj.getLink())
                .description(obj.getDescription())
                .build();
        return moreInfoLinkRepository.save(moreInfoLink);
    }

    public void delete(Long id) {
        findById(id);
        moreInfoLinkRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return moreInfoLinkRepository.existsById(id);
    }
}
