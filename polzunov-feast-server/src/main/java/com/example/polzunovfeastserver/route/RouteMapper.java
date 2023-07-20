package com.example.polzunovfeastserver.route;

import com.example.polzunovfeastserver.route.entity.RouteEntity;
import com.example.polzunovfeastserver.route.node.RouteNodeMapper;
import org.openapitools.model.RouteNodeWithEventResponse;
import org.openapitools.model.RouteWithEventResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

public final class RouteMapper {
    private RouteMapper() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static RouteWithEventResponse toRouteWithEvent(RouteEntity routeEntity) {
        RouteWithEventResponse route = new RouteWithEventResponse();
        List<RouteNodeWithEventResponse> routeNodes = routeEntity.getRouteNodes()
                .stream().map(RouteNodeMapper::toNodeWithEvent).collect(toList());
        route.setNodes(routeNodes);
        return route;
    }
}
