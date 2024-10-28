package br.ufpb.dcx.apps4society.meuguiapbapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}
