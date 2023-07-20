package com.example.polzunovfeastserver.event;

import com.example.polzunovfeastserver.event.entity.EventEntity;
import com.example.polzunovfeastserver.event.exception.EventNotFoundException;
import com.example.polzunovfeastserver.place.PlaceEntityRepository;
import com.example.polzunovfeastserver.place.PlaceMapper;
import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import com.example.polzunovfeastserver.place.excepition.PlaceNotFoundException;
import org.openapitools.model.Event;
import org.openapitools.model.EventWithPlaceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EventService {

    private final EventEntityRepository eventEntityRepo;
    private final PlaceEntityRepository placeEntityRepo;

    public EventService(EventEntityRepository eventEntityRepo, PlaceEntityRepository placeEntityRepo) {
        this.eventEntityRepo = eventEntityRepo;
        this.placeEntityRepo = placeEntityRepo;
    }

    public EventWithPlaceResponse addEvent(Event event) {
        event.setId(null);
        PlaceEntity placeEntity = getPlaceEntityById(event.getPlaceId());
        EventEntity eventEntity = eventEntityRepo.save(EventMapper.toEventEntity(event, placeEntity));
        return EventMapper.toEventWithPlaceResponse(eventEntity, PlaceMapper.toPlace(eventEntity.getPlace()));
    }

    public EventWithPlaceResponse updateEventById(Event event) {
        if (!eventEntityRepo.existsById(event.getId())) {
            throw new EventNotFoundException(
                    String.format("Cannot update event with id=%d, because event not found", event.getId())
            );
        }
        PlaceEntity placeEntity = getPlaceEntityById(event.getPlaceId());
        EventEntity eventEntity = eventEntityRepo.save(EventMapper.toEventEntity(event, placeEntity));
        return EventMapper.toEventWithPlaceResponse(eventEntity, PlaceMapper.toPlace(eventEntity.getPlace()));
    }

    public List<EventWithPlaceResponse> getAllEvents(Integer page, Integer size) {
        Page<EventEntity> events = eventEntityRepo.findAll(PageRequest.of(page, size));
        return events.stream().map(ev ->
                EventMapper.toEventWithPlaceResponse(ev, PlaceMapper.toPlace(ev.getPlace()))
        ).toList();
    }

    public EventWithPlaceResponse getEventById(Long id) {
        EventEntity eventEntity = eventEntityRepo.findById(id).orElseThrow(() -> new EventNotFoundException(
                String.format("Cannot get event with id=%d, because event not found", id)
        ));
        return EventMapper.toEventWithPlaceResponse(eventEntity, PlaceMapper.toPlace(eventEntity.getPlace()));
    }

    public void deleteEventById(Long id) {
        if (!eventEntityRepo.existsById(id)) {
            return;
        }
        eventEntityRepo.deleteById(id);
    }

    private PlaceEntity getPlaceEntityById(Long id) {
        return placeEntityRepo.findById(id).orElseThrow(() -> new PlaceNotFoundException(
                String.format("Place with id=%d not found", id)
        ));
    }
}
