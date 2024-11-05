package br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
public class AuthenticationForm {
    private String email;
    private String password;
}
