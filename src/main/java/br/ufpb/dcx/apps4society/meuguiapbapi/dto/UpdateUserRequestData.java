package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class UpdateUserRequestData {
    @NotBlank(
            message = "O email não pode ser vazio"
    )
    @Email (
            message = "'${validatedValue}' não é um email válido"
    )
    private String email;

    @NotBlank(
            message = "O nome não pode ser vazio"
    )
    private String firstName;

    @NotBlank(
            message = "O sobrenome não pode ser vazio"
    )
    private String lastName;

    public UpdateUserRequestData(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UpdateUserRequestData() {
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public String getEmail() {
        return this.email;
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

        UpdateUserRequestData that = (UpdateUserRequestData) o;
        return Objects.equals(email, that.email) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(firstName);
        result = 31 * result + Objects.hashCode(lastName);
        return result;
    }

    public String toString() {
        return "UserDTO(email=" + this.getEmail() + ", firstName=" + this.getFirstName() + ", lastName=" + this.getLastName() + ")";
    }

    public static class UserDTOBuilder {
        private String email;
        private String firstName;
        private String lastName;

        UserDTOBuilder() {
        }

        public UserDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UpdateUserRequestData build() {
            return new UpdateUserRequestData(this.email, this.firstName, this.lastName);
        }

        public String toString() {
            return "UserDTO.UserDTOBuilder(email=" + this.email + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ")";
        }
    }
}
