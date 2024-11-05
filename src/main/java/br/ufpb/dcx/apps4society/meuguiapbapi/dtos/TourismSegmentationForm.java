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
public class TourismSegmentationForm {
    @NotBlank(
            message = "O nome do segmento turístico é obrigatório"
    )
    private String name;
    private String description;
}
