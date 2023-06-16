package com.example.polzunovfeastserver.event;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.EventApi;
import org.openapitools.model.Event;
import org.openapitools.model.EventWithPlaceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class EventController implements EventApi {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public ResponseEntity<EventWithPlaceResponse> addEvent(Event event) {
        EventWithPlaceResponse newEvent = eventService.addEvent(event);
        log.info("Event with id={} added", newEvent.getId());
        return ResponseEntity.ok(newEvent);
    }

    @Override
    public ResponseEntity<EventWithPlaceResponse> updateEventById(Event event) {
       EventWithPlaceResponse updatedEvent = eventService.updateEventById(event);
       log.info("Event with id={} updated", updatedEvent.getId());
       return ResponseEntity.ok(updatedEvent);
    }

    @Override
    public ResponseEntity<List<EventWithPlaceResponse>> getAllEvents(Integer page, Integer size) {
        List<EventWithPlaceResponse> events = eventService.getAllEvents(page, size);
        log.info("Events page of size={} with index={} returned, actual size={}", size, page, events.size());
        return ResponseEntity.ok(events);
    }

    @Override
    public ResponseEntity<EventWithPlaceResponse> getEventById(Long id) {
        EventWithPlaceResponse event = eventService.getEventById(id);
        log.info("Event with id={} returned", event.getId());
        return ResponseEntity.ok(event);
    }

    @Override
    public ResponseEntity<Void> deleteEventById(Long id) {
        eventService.deleteEventById(id);
        log.info("Event with id={} deleted", id);
        return ResponseEntity.ok().build();
    }
}
