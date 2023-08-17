package com.example.polzunovfeastserver.route.util;

public final class RoutesTableKeys {

    private RoutesTableKeys() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static final String UNIQUE_OWNER = "routes_ukey_owner_id";
    public static final String FOREIGN_OWNER = "routes_fkey_owner_id";
}
