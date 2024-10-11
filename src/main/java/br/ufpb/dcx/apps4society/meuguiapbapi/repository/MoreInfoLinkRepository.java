package br.ufpb.dcx.apps4society.meuguiapbapi.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoreInfoLinkRepository extends JpaRepository<MoreInfoLink, Long> {
}
