package com.example.polzunovfeastserver.event;

import com.example.polzunovfeastserver.category.CategoryMapper;
import com.example.polzunovfeastserver.category.CategoryService;
import com.example.polzunovfeastserver.category.entity.CategoryEntity;
import com.example.polzunovfeastserver.event.entity.EventEntity;
import com.example.polzunovfeastserver.event.exception.EventNotFoundException;
import com.example.polzunovfeastserver.event.exception.EventUpdateRestrictedException;
import com.example.polzunovfeastserver.place.PlaceService;
import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import com.example.polzunovfeastserver.place.excepition.PlaceNotFoundException;
import com.example.polzunovfeastserver.route.node.RouteNodeEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Category;
import org.openapitools.model.Event;
import org.openapitools.model.EventWithPlaceResponse;
import org.openapitools.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import static com.example.polzunovfeastserver.event.util.EventEntitySpecifications.where;
import static com.example.polzunovfeastserver.event.EventMapper.toEventEntity;
import static com.example.polzunovfeastserver.event.EventMapper.toEventWithPlaceResponse;
import static com.example.polzunovfeastserver.place.PlaceMapper.toPlace;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventEntityRepository eventRepo;
    private final PlaceService placeService;
    private final RouteNodeEntityRepository nodeRepo;
    private final CategoryService categoryService;

    /**
     * @throws PlaceNotFoundException place not found
     */
    public EventWithPlaceResponse addEvent(Event event) {
        event.setId(null);
        event.setCanceled(false);

        PlaceEntity placeEntity = placeService.getEntityById(event.getPlaceId());
        Set<CategoryEntity> categoryEntities = categoryService.getAllEntitiesById(event.getCategoryIds());
        EventEntity eventEntity = eventRepo.save(toEventEntity(event, placeEntity, categoryEntities));

        Set<Category> categories = eventEntity.getCategories().stream().map(CategoryMapper::toCategory).collect(toSet());
        return toEventWithPlaceResponse(eventEntity, toPlace(placeEntity), categories);
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
            eventEntity.setPlace(placeService.getEntityById(event.getPlaceId()));
        }

        //We save 'canceled' before update because we need to send notifications only after data was saved.
        boolean entityCanceled = eventEntity.isCanceled();

        Set<CategoryEntity> categoryEntities = categoryService.getAllEntitiesById(event.getCategoryIds());
        eventEntity = eventRepo.save(toEventEntity(event, eventEntity.getPlace(), categoryEntities));

        if (!entityCanceled && event.getCanceled()) {
            //TODO send notification that event is canceled
            log.info("Sending notification that event with id={} is canceled", event.getId());
        } else if (entityCanceled && !event.getCanceled()) {
            //TODO send notification that event is not canceled again
            log.info("Sending notification that event with id={} is not canceled again", event.getId());
        }

        Place place = toPlace(eventEntity.getPlace());
        Set<Category> categories = eventEntity.getCategories().stream().map(CategoryMapper::toCategory).collect(toSet());
        return toEventWithPlaceResponse(eventEntity, place, categories);
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

    public List<EventWithPlaceResponse> getAllEvents(EventParameter eventParameter, Integer page, Integer size) {


        Page<EventEntity> events = eventRepo.findAll(where(eventParameter), PageRequest.of(page, size));
        return events.stream().map(ev ->
                toEventWithPlaceResponse(
                        ev,
                        toPlace(ev.getPlace()),
                        ev.getCategories().stream().map(CategoryMapper::toCategory).collect(toSet())
                )
        ).toList();
    }

    public EventWithPlaceResponse getEventById(Long id) {
        EventEntity eventEntity = eventRepo.findById(id).orElseThrow(() -> new EventNotFoundException(
                format("Cannot get event with id=%d, because event not found", id)
        ));
        return toEventWithPlaceResponse(
                eventEntity,
                toPlace(eventEntity.getPlace()),
                eventEntity.getCategories().stream().map(CategoryMapper::toCategory).collect(toSet()));
    }

    public void deleteEventById(Long id) {
        if (!eventRepo.existsById(id)) {
            return;
        }
        eventRepo.deleteById(id);
    }
}
