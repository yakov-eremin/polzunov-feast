package com.example.polzunovfeastserver.event;

import com.example.polzunovfeastserver.event.entity.EventEntity;
import com.example.polzunovfeastserver.event.exception.EventNotFoundException;
import com.example.polzunovfeastserver.event.exception.EventUpdateRestrictedException;
import com.example.polzunovfeastserver.place.PlaceEntityRepository;
import com.example.polzunovfeastserver.place.PlaceMapper;
import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import com.example.polzunovfeastserver.place.excepition.PlaceNotFoundException;
import com.example.polzunovfeastserver.route.node.RouteNodeEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Event;
import org.openapitools.model.EventWithPlaceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventEntityRepository eventRepo;
    private final PlaceEntityRepository placeRepo;
    private final RouteNodeEntityRepository nodeRepo;

    public EventWithPlaceResponse addEvent(Event event) {
        event.setId(null);
        event.setCanceled(false);
        PlaceEntity placeEntity = getPlaceEntityById(event.getPlaceId());
        EventEntity eventEntity = eventRepo.save(EventMapper.toEventEntity(event, placeEntity));
        return EventMapper.toEventWithPlaceResponse(eventEntity, PlaceMapper.toPlace(eventEntity.getPlace()));
    }

    /**
     * @throws EventNotFoundException         event not found
     * @throws PlaceNotFoundException         place not found
     * @throws EventUpdateRestrictedException It's already started, or it's in someone's route.
     *                                        This checks will not be performed if event is canceled
     */
    public EventWithPlaceResponse updateEventById(Event event) {
        EventEntity eventEntity = eventRepo.findById(event.getId()).orElseThrow(() -> new EventNotFoundException(
                format("Cannot update event with id=%d, because event not found", event.getId())
        ));

        checkThatCanBeUpdated(eventEntity, event);

        if (!event.getPlaceId().equals(eventEntity.getPlace().getId())) {
            eventEntity.setPlace(getPlaceEntityById(event.getPlaceId()));
        }

        //We save 'canceled' before update because we need to send notifications only after data was saved.
        boolean entityCanceled = eventEntity.isCanceled();

        eventEntity = eventRepo.save(EventMapper.toEventEntity(event, eventEntity.getPlace()));

        if (!entityCanceled && event.getCanceled()) {
            //TODO send notification that event is canceled
            log.info("Sending notification that event with id={} is canceled", event.getId());
        } else if (entityCanceled && !event.getCanceled()) {
            //TODO send notification that event is not canceled again
            log.info("Sending notification that event with id={} is not canceled again", event.getId());
        }

        return EventMapper.toEventWithPlaceResponse(eventEntity, PlaceMapper.toPlace(eventEntity.getPlace()));
    }

    /**
     * Checks will not be performed if event is canceled. Because in that case it can be updated anyway
     * (except those validations that described in validation annotations) .
     * <br>
     * <br>
     * Not canceled event can be updated only if:
     * <ul>
     *     <li>it hasn't already started</li>
     *     <li>it's not in someone's route</li>
     * </ul>
     *
     * @param currentEvent data from db
     * @param newEvent     data from request
     * @throws EventUpdateRestrictedException It's already started, or it's in someone's route.
     */
    private void checkThatCanBeUpdated(EventEntity currentEvent, Event newEvent) {
        if (currentEvent.isCanceled()) {
            return;
        }

        //check if it's already started
        OffsetDateTime now = OffsetDateTime.now();
        if (newEvent.getStartTime().isBefore(now) || newEvent.getStartTime().isEqual(now)) {
            throw new EventUpdateRestrictedException(
                    format("Cannot update event with id=%d, because it's already started, " +
                            "event start time = '%s'", newEvent.getId(), newEvent.getStartTime()));
        }

        //check if it's in someone's route
        if (nodeRepo.existsByEventId(newEvent.getId())) {
            throw new EventUpdateRestrictedException(
                    format("Cannot update event with id=%d, because it's in someone's route.", newEvent.getId()));
        }
    }

    public List<EventWithPlaceResponse> getAllEvents(Integer page, Integer size) {
        Page<EventEntity> events = eventRepo.findAll(PageRequest.of(page, size));
        return events.stream().map(ev ->
                EventMapper.toEventWithPlaceResponse(ev, PlaceMapper.toPlace(ev.getPlace()))
        ).toList();
    }

    public EventWithPlaceResponse getEventById(Long id) {
        EventEntity eventEntity = eventRepo.findById(id).orElseThrow(() -> new EventNotFoundException(
                format("Cannot get event with id=%d, because event not found", id)
        ));
        return EventMapper.toEventWithPlaceResponse(eventEntity, PlaceMapper.toPlace(eventEntity.getPlace()));
    }

    public void deleteEventById(Long id) {
        if (!eventRepo.existsById(id)) {
            return;
        }
        eventRepo.deleteById(id);
    }

    /**
     * @throws PlaceNotFoundException place not found
     */
    private PlaceEntity getPlaceEntityById(Long id) {
        return placeRepo.findById(id).orElseThrow(() -> new PlaceNotFoundException(
                format("Place with id=%d not found", id)
        ));
    }
}
