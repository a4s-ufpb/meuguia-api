package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.dtos.MoreInfoLinkForm;

public class MockMoreInfoLink {
    private static MockMoreInfoLink instance;

    public static MockMoreInfoLink getInstance(){
        if (instance == null) {
            instance = new MockMoreInfoLink();
        }
        return instance;
    }

    public MoreInfoLink mockEntity(Integer num) {
        return MoreInfoLink.builder()
                .id(num.longValue())
                .link("https://www.mock-link"+num+".com")
                .description("description")
                .build();
    }

    public MoreInfoLinkForm mockRequest(Integer num) {
        return MoreInfoLinkForm.builder()
                .link("https://www.mock-link"+num+".com")
                .description("description")
                .build();

    }
}
