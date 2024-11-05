package br.ufpb.dcx.apps4society.meuguiapbapi.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttractionTypeForm {
    @NotBlank(
            message = "O nome do tipo de atrativo é obrigatório"
    )
    private String name;
    private String description;
}
