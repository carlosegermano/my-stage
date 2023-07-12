package com.cesg.stage.enums;

public enum Role {

    ROLE_MUSICIAN("musician"), ROLE_LISTENER("listener");

    private String role;

    public String getRole() {
        return role;
    }
    Role(String role) {
        this.role = role;
    }

}
