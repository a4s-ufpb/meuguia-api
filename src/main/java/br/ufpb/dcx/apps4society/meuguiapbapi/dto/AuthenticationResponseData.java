package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import java.util.Objects;

public class AuthenticationResponseData {
    private String token;

    public AuthenticationResponseData(String token) {
        this.token = token;
    }

    public AuthenticationResponseData() {
    }

    public static AuthenticationResponseDataBuilder builder() {
        return new AuthenticationResponseDataBuilder();
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthenticationResponseData that = (AuthenticationResponseData) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token);
    }

    public String toString() {
        return "AuthenticationResponseData(token=" + this.getToken() + ")";
    }

    public static class AuthenticationResponseDataBuilder {
        private String token;

        AuthenticationResponseDataBuilder() {
        }

        public AuthenticationResponseDataBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthenticationResponseData build() {
            return new AuthenticationResponseData(this.token);
        }

        public String toString() {
            return "AuthenticationResponseData.AuthenticationResponseDataBuilder(token=" + this.token + ")";
        }
    }
}
