package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.MoreInfoLinkRequestData;

import java.util.List;

public class MoreInfoLinkTestHelper {
    private static MoreInfoLinkTestHelper instance;

    public static MoreInfoLinkTestHelper getInstance(){
        if (instance == null) {
            instance = new MoreInfoLinkTestHelper();
        }
        return instance;
    }

    public MoreInfoLink createMoreInfoLink(Integer id) {
        return MoreInfoLink.builder()
                .id(id.longValue())
                .link("https://www.mock-link"+id+".com")
                .description("description")
                .build();
    }

    public MoreInfoLinkRequestData createMoreInfoLinkRequestData(Integer num) {
        return MoreInfoLinkRequestData.builder()
                .link("https://www.mock-link"+num+".com")
                .description("description")
                .build();

    }

    public List<MoreInfoLink> getListOfMoreInfoLinks() {
        return List.of(
                createMoreInfoLink(1),
                createMoreInfoLink(2),
                createMoreInfoLink(3)
        );
    }
}
