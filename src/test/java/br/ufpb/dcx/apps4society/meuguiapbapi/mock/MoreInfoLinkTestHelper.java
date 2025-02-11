package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.moreinfolink.MoreInfoLinkDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.moreinfolink.MoreInfoLinkRequestData;

import java.util.List;

public class MoreInfoLinkTestHelper {
    private static MoreInfoLinkTestHelper instance;

    public static MoreInfoLinkTestHelper getInstance(){
        if (instance == null) {
            instance = new MoreInfoLinkTestHelper();
        }
        return instance;
    }

    public MoreInfoLink createMoreInfoLink(Integer num) {
        return MoreInfoLink.builder()
                .link("https://www.mock-link"+num+".com")
                .description("description")
                .build();
    }

    public MoreInfoLinkDTO createMoreInfoLinkDTO(Integer num) {
        return MoreInfoLinkDTO.builder()
                .link("https://www.mock-link"+num+".com")
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

    public List<MoreInfoLinkDTO> getListOfMoreInfoLinksDTO() {
        return List.of(
                createMoreInfoLinkDTO(1),
                createMoreInfoLinkDTO(2),
                createMoreInfoLinkDTO(3)
        );
    }

    public List<MoreInfoLinkRequestData> getListOfMoreInfoLinksRequestData() {
        return List.of(
                createMoreInfoLinkRequestData(1),
                createMoreInfoLinkRequestData(2),
                createMoreInfoLinkRequestData(3)
        );
    }
}
