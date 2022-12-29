package com.example.polzunovfeastserver.controller;

import org.openapitools.api.RouteApi;
import org.openapitools.model.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController implements RouteApi {
    @Override
    public ResponseEntity<Route> getUserRoute() {
        return ResponseEntity.ok(new Route());
    }
}
