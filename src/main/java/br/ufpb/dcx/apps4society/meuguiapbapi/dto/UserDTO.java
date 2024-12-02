package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import java.util.Objects;

public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;

    public UserDTO(Long id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserDTO() {
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public Long getId() {
        return this.id;
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

    public void setId(Long id) {
        this.id = id;
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

        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(email, userDTO.email) && Objects.equals(firstName, userDTO.firstName) && Objects.equals(lastName, userDTO.lastName);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(firstName);
        result = 31 * result + Objects.hashCode(lastName);
        return result;
    }

    public String toString() {
        return "UserDTO(id=" + this.getId() + ", email=" + this.getEmail() + ", firstName=" + this.getFirstName() + ", lastName=" + this.getLastName() + ")";
    }

    public static class UserDTOBuilder {
        private Long id;
        private String email;
        private String firstName;
        private String lastName;

        UserDTOBuilder() {
        }

        public UserDTOBuilder id(Long id) {
            this.id = id;
            return this;
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

        public UserDTO build() {
            return new UserDTO(this.id, this.email, this.firstName, this.lastName);
        }

        public String toString() {
            return "UserDTO.UserDTOBuilder(id=" + this.id + ", email=" + this.email + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ")";
        }
    }
}
