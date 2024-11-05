package br.ufpb.dcx.apps4society.meuguiapbapi.dtos;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttractionForm {
    @NotBlank(
            message = "Nome da Atração é obrigatório"
    )
    private String name;

    private String description;

    private String map_link;

    @NotBlank(
            message = "Cidade da Atração é obrigatória"
    )
    private String city;

    @NotBlank(
            message = "Estado da Atração é obrigatório"
    )
    private String state;

    @NotEmpty(
            message = "Link da Imagem é obrigatório"
    )
    @Pattern(
            regexp = "^(https?)://[^\\s/$.?#].\\S*$",
            message = "Link da Imagem inválido. Link deve iniciar com http ou https"
    )
    private String image_link;

    private String fonte;

    @NotEmpty(
            message = "Segmentações da Atração são obrigatórias"
    )
    private List<@Valid TourismSegmentation> segmentations;

    @NotNull(
            message = "Tipos de Atração são obrigatórios"
    )
    private @Valid AttractionType attractionTypes;

    @NotEmpty(
            message = "Links de Mais Informações são obrigatórios"
    )
    private List<@Valid MoreInfoLink> moreInfoLinkList;
}
