package com.example.polzunovfeastserver.controller;

import org.openapitools.api.EventApi;
import org.openapitools.model.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController implements EventApi {
    @Override
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(List.of(new Event()));
    }
}
