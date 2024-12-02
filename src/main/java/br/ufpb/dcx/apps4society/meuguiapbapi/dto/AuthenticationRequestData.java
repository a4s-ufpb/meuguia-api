package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.Objects;


public class AuthenticationRequestData {
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

    public AuthenticationRequestData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthenticationRequestData() {
    }

    public static AuthenticationRequestDataBuilder builder() {
        return new AuthenticationRequestDataBuilder();
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthenticationRequestData that = (AuthenticationRequestData) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(password);
        return result;
    }

    public String toString() {
        return "AuthenticationRequestData(email=" + this.getEmail() + ", password=" + this.getPassword() + ")";
    }

    public static class AuthenticationRequestDataBuilder {
        private String email;
        private String password;

        AuthenticationRequestDataBuilder() {
        }

        public AuthenticationRequestDataBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AuthenticationRequestDataBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AuthenticationRequestData build() {
            return new AuthenticationRequestData(this.email, this.password);
        }

        public String toString() {
            return "AuthenticationRequestData.AuthenticationRequestDataBuilder(email=" + this.email + ", password=" + this.password + ")";
        }
    }
}
