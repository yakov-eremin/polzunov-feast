package com.example.polzunovfeastserver.route.node.util;

public final class RouteNodesTableKeys {
    private RouteNodesTableKeys() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static final String FOREIGN_EVENT = "route_nodes_fkey_event_id";
    public static final String FOREIGN_ROUTE = "route_nodes_fkey_route_id";
}
