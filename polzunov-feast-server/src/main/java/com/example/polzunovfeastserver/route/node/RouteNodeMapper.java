package com.example.polzunovfeastserver.route.node;

import com.example.polzunovfeastserver.event.EventMapper;
import com.example.polzunovfeastserver.place.PlaceMapper;
import com.example.polzunovfeastserver.route.node.entity.RouteNodeEntity;
import org.openapitools.model.EventWithPlaceResponse;
import org.openapitools.model.RouteNodeWithEventResponse;

public final class RouteNodeMapper {
    private RouteNodeMapper() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static RouteNodeWithEventResponse toNodeWithEvent(RouteNodeEntity routeNodeEntity) {
        RouteNodeWithEventResponse routeNode = new RouteNodeWithEventResponse();
        EventWithPlaceResponse eventWithPlaceResponse = EventMapper.toEventWithPlaceResponse(
                routeNodeEntity.getEvent(),
                PlaceMapper.toPlace(routeNodeEntity.getEvent().getPlace())
        );
        routeNode.setEvent(eventWithPlaceResponse);
        return routeNode;
    }
}
