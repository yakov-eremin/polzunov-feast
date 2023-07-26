package com.example.polzunovfeastserver.user.entity;

public enum Role {
    USER,
    ADMIN,
    ROOT;

    /**
     * @return the name of the role with SCOPE_ prefix, e.g. SCOPE_USER
     */
    public String asScope() {
        return "SCOPE_" + name();
    }
}
