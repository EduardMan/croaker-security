package tech.itparklessons.fileshares.user.model.enums;

public enum Role {
    USER,
    ADMIN;

    public String getSpringRole() {
        return "ROLE_" + this.name();
    }
}
