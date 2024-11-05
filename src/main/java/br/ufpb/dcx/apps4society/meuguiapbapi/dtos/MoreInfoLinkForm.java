package br.ufpb.dcx.apps4society.meuguiapbapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoreInfoLinkForm {
    @NotBlank(
            message = "O link não pode ser vazio"
    )
    @Pattern(
            regexp = "^(https?|ftp)://[^\\s/$.?#].\\S*$",
            message = "URL inválida"
    )
    private String link;
    private String description;
}
