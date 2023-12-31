package com.firs.risk.mgt.auth.svc.enums;

public enum RoleEnum {
    ADMIN("ADMIN"),
    USER("USER");

    private String value;

    private RoleEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}