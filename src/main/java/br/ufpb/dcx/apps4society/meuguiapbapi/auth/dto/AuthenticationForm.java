package br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationForm {
    @NotBlank(
            message = "O email não pode ser vazio"
    )
    @Email(
            message = "'${validatedValue}' não é um email válido"
    )
    private String email;

    @NotBlank(
        message = "A senha não pode ser vazia"
    )
    @Size(
            min = 8,
            message = "A senha deve ter no mínimo ${min} caracteres"
    )
    private String password;
}
