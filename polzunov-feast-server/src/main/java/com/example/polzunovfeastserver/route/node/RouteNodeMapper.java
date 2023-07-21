package com.example.polzunovfeastserver.route.node;

import com.example.polzunovfeastserver.event.entity.EventEntity;
import com.example.polzunovfeastserver.route.node.entity.RouteNodeEntity;
import org.openapitools.model.EventWithPlaceResponse;
import org.openapitools.model.RouteNodeWithEventResponse;

public final class RouteNodeMapper {
    private RouteNodeMapper() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static RouteNodeWithEventResponse toRouteNodeWithEvent(EventWithPlaceResponse eventWithPlaceResponse) {
        RouteNodeWithEventResponse routeNode = new RouteNodeWithEventResponse();
        routeNode.setEvent(eventWithPlaceResponse);
        return routeNode;
    }

    public static RouteNodeEntity toRouteNodeEntity(Long id, EventEntity eventEntity) {
        return new RouteNodeEntity(
                id,
                eventEntity
        );
    }
}
