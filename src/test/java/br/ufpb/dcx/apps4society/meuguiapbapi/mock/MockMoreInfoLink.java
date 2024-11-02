package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;

public class MockMoreInfoLink {
    public MoreInfoLink mockEntity(Integer num) {
        return MoreInfoLink.builder()
                .id(num.longValue())
                .link("https://www.mock-link"+num+".com")
                .description("description")
                .build();
    }

    public MoreInfoLink mockRequest(Integer num) {
        return MoreInfoLink.builder()
                .link("https://www.mock-link"+num+".com")
                .description("description")
                .build();

    }
}
