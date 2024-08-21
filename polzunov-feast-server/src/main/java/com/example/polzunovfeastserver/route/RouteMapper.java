package com.example.polzunovfeastserver.route;

import org.openapitools.model.RouteNodeWithEventResponse;
import org.openapitools.model.RouteWithEventResponse;

import java.util.List;

public final class RouteMapper {
    private RouteMapper() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static RouteWithEventResponse toRouteWithEventResponse(List<RouteNodeWithEventResponse> routeNodes) {
        return new RouteWithEventResponse(routeNodes);
    }
}
