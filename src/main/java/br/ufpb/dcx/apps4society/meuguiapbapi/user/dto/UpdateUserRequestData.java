package br.ufpb.dcx.apps4society.meuguiapbapi.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class UpdateUserRequestData {
    @NotBlank(
            message = "O email não pode ser vazio"
    )
    @Email(
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

    public static UpdateUserRequestDataBuilder builder() {
        return new UpdateUserRequestDataBuilder();
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(firstName);
        result = 31 * result + Objects.hashCode(lastName);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateUserRequestData that = (UpdateUserRequestData) o;
        return Objects.equals(email, that.email) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName);
    }

    public String toString() {
        return "UserDTO(email=" + this.getEmail() + ", firstName=" + this.getFirstName() + ", lastName=" + this.getLastName() + ")";
    }

    public static final class UpdateUserRequestDataBuilder {
        private @NotBlank(
                message = "O email não pode ser vazio"
        )
        @Email(
                message = "'${validatedValue}' não é um email válido"
        ) String email;
        private @NotBlank(
                message = "O nome não pode ser vazio"
        ) String firstName;
        private @NotBlank(
                message = "O sobrenome não pode ser vazio"
        ) String lastName;

        private UpdateUserRequestDataBuilder() {
        }

        public static UpdateUserRequestDataBuilder anUpdateUserRequestData() {
            return new UpdateUserRequestDataBuilder();
        }

        public UpdateUserRequestDataBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UpdateUserRequestDataBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UpdateUserRequestDataBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UpdateUserRequestData build() {
            UpdateUserRequestData updateUserRequestData = new UpdateUserRequestData();
            updateUserRequestData.setEmail(email);
            updateUserRequestData.setFirstName(firstName);
            updateUserRequestData.setLastName(lastName);
            return updateUserRequestData;
        }
    }
}
