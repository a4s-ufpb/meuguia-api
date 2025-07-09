package br.ufpb.dcx.apps4society.meuguiapbapi.user.domain;

public enum UserRole {
    SUPER_ADMIN("SUPER_ADMIN"),
    ADMIN("ADMIN"),
    DEFAULT_USER("DEFAULT_USER"),
    GUEST("GUEST");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String role() {
        return role;
    }

    @Override
    public String toString() {
        return role;
    }
}
