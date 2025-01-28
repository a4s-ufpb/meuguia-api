package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class RegisterUserRequestData {
    @NotBlank(
        message = "O email não pode ser vazio"
    )
    @Email (
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

    @NotBlank(
        message = "O nome não pode ser vazio"
    )
    private String firstName;

    @NotBlank(
        message = "O sobrenome não pode ser vazio"
    )
    private String lastName;

    public RegisterUserRequestData(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public RegisterUserRequestData() {
    }

    public static RegisterRequestDataBuilder builder() {
        return new RegisterRequestDataBuilder();
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisterUserRequestData that = (RegisterUserRequestData) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(password);
        result = 31 * result + Objects.hashCode(firstName);
        result = 31 * result + Objects.hashCode(lastName);
        return result;
    }

    public String toString() {
        return "RegisterRequestData(email=" + this.getEmail() + ", password=" + this.getPassword() + ", firstName=" + this.getFirstName() + ", lastName=" + this.getLastName() + ")";
    }

    public static class RegisterRequestDataBuilder {
        private String email;
        private String password;
        private String firstName;
        private String lastName;

        RegisterRequestDataBuilder() {
        }

        public RegisterRequestDataBuilder email(String email) {
            this.email = email;
            return this;
        }

        public RegisterRequestDataBuilder password(String password) {
            this.password = password;
            return this;
        }

        public RegisterRequestDataBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public RegisterRequestDataBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public RegisterUserRequestData build() {
            return new RegisterUserRequestData(this.email, this.password, this.firstName, this.lastName);
        }

        public String toString() {
            return "RegisterRequestData.RegisterRequestDataBuilder(email=" + this.email + ", password=" + this.password + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ")";
        }
    }
}
