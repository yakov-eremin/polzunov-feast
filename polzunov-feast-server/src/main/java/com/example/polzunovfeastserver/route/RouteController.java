package com.example.polzunovfeastserver.route;


import com.example.polzunovfeastserver.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.RouteApi;
import org.openapitools.model.Route;
import org.openapitools.model.RouteWithEventResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RouteController implements RouteApi {

    private final RouteService routeService;

    @Override
    public ResponseEntity<RouteWithEventResponse> getRouteByUserId() {
        Long userId = AuthenticationUtils.extractUserIdFromToken();
        RouteWithEventResponse route = routeService.getRouteByOwnerId(userId);
        log.info("Route owned by user with id={} returned", userId);
        return ResponseEntity.ok(route);
    }

    @Override
    public ResponseEntity<RouteWithEventResponse> updateRouteByUserId(Route route) {
        Long userId = AuthenticationUtils.extractUserIdFromToken();
        RouteWithEventResponse routeWithEventResponse = routeService.updateRouteByOwnerId(route, userId);
        log.info("Route owned by user with id={} updated", userId);
        return ResponseEntity.ok(routeWithEventResponse);
    }
}
