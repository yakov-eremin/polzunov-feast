package com.example.polzunovfeastserver.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.EventApi;
import org.openapitools.model.Event;
import org.openapitools.model.EventWithPlaceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController implements EventApi {

    private final EventService eventService;

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
    public ResponseEntity<String> addEventImage(Long id, MultipartFile image, Boolean main) {
        String imageUrl = eventService.addEventImage(id, image, main);
        log.info("Added an image to event with id={}", id);
        return ResponseEntity.ok(imageUrl);
    }

    @Override
    public ResponseEntity<List<EventWithPlaceResponse>> getAllEvents(List<Long> catIds, Integer age,
                                                                     OffsetDateTime start, OffsetDateTime end,
                                                                     Boolean canceled,
                                                                     Integer page, Integer size) {
        var eventParameter = EventParameter.builder()
                .categoryIds(catIds)
                .ageLimit(age)
                .canceled(canceled)
                .start(start)
                .end(end)
                .build();
        List<EventWithPlaceResponse> events = eventService.getAllEvents(eventParameter, page, size);
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
