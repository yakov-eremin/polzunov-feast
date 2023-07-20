package com.example.polzunovfeastserver.route;

import com.example.polzunovfeastserver.event.EventEntityRepository;
import com.example.polzunovfeastserver.event.entity.EventEntity;
import com.example.polzunovfeastserver.event.exception.EventNotFoundException;
import com.example.polzunovfeastserver.route.entity.RouteEntity;
import com.example.polzunovfeastserver.route.exception.RouteUpdateRestrictedException;
import com.example.polzunovfeastserver.route.node.entity.RouteNodeEntity;
import com.example.polzunovfeastserver.user.UserService;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import com.example.polzunovfeastserver.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Route;
import org.openapitools.model.RouteNode;
import org.openapitools.model.RouteWithEventResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RouteService {
    private final RouteEntityRepository routeRepo;
    private final EventEntityRepository eventRepo;
    private final UserService userService;

    /**
     * If route doesn't exist, it will be created.
     *
     * @return Route with nodes sorted by events start time asc
     * @throws UserNotFoundException if user doesn't exist
     */
    public RouteWithEventResponse getRouteByOwnerId(Long ownerId) {
        UserEntity owner = userService.getEntityById(ownerId);
        Optional<RouteEntity> routeEntityOpt = routeRepo.findByOwner_Id(ownerId);
        RouteEntity routeEntity = routeEntityOpt.orElseGet(() -> {
                    log.info("Route owned by user with id={} didn't exist, creating it", ownerId);
                    return routeRepo.save(new RouteEntity(null, owner, List.of()));
                }
        );

        RouteWithEventResponse routeWithEventResponse = RouteMapper.toRouteWithEvent(routeEntity);
        routeWithEventResponse.getNodes().sort(Comparator.comparing(n -> n.getEvent().getStartTime()));
        return routeWithEventResponse;
    }

    /**
     * If route doesn't exist, it will be created.
     * To remove all nodes form route send route with empty nodes list.
     * If route contains duplicated events, it will be ignored and route will be updated.
     *
     * @return Route with nodes sorted by events start time asc
     * @throws UserNotFoundException          if user doesn't exist
     * @throws EventNotFoundException         if some events not found
     * @throws RouteUpdateRestrictedException if some events have already ended
     */
    public RouteWithEventResponse updateRouteByOwnerId(Route route, Long ownerId) {
        UserEntity owner = userService.getEntityById(ownerId);

        List<Long> eventIds = route.getNodes().stream().map(RouteNode::getEventId).toList();
        List<EventEntity> events = eventRepo.findAllByIdOrderByStartTimeAsc(eventIds);

        //check that all events exist
        if (events.size() != eventIds.size()) {
            Set<Long> notFoundIds = new HashSet<>(eventIds);
            notFoundIds.removeAll(events.stream().map(EventEntity::getId).collect(toSet()));

            if (!notFoundIds.isEmpty()) {
                throw new EventNotFoundException(format("Cannot update route, because events with this ids not found: %s", notFoundIds));
            } else { //the fact that set is empty means that all events found, but eventIds contain duplicates, which will be ignored
                log.warn("User with id={} send duplicated event ids when updating his route. Events ids: {}", ownerId, eventIds);
            }
        }

        checkEventsTime(events);

        List<RouteNodeEntity> routeNodes = events.stream().map(e -> new RouteNodeEntity(null, e)).toList();

        Optional<RouteEntity> routeEntityOpt = routeRepo.findByOwner_Id(ownerId);
        RouteEntity routeEntity;
        if (routeEntityOpt.isPresent()) {
            routeEntity = routeEntityOpt.get();
            routeEntity.getRouteNodes().clear(); //clear old nodes
            routeEntity.getRouteNodes().addAll(routeNodes);// and save updated
        } else {
            routeEntity = new RouteEntity(null, owner, routeNodes); //create new route, if it didn't exist
        }
        routeEntity = routeRepo.save(routeEntity);


        RouteWithEventResponse routeWithEventResponse = RouteMapper.toRouteWithEvent(routeEntity);
        routeWithEventResponse.getNodes().sort(Comparator.comparing(n -> n.getEvent().getStartTime()));
        return routeWithEventResponse;
    }

    /**
     * Checks that events haven't started and doesn't overlap each other
     * (if end time of earlier event is after or equal to start time of next event)
     *
     * @param events must be sorted by start time acs
     * @throws RouteUpdateRestrictedException if some events have already ended or events overlap each other
     */
    private void checkEventsTime(List<EventEntity> events) {
        OffsetDateTime now = OffsetDateTime.now();

        for (int i = 0; i < events.size(); i++) {
            EventEntity currentEvent = events.get(i);
            if (currentEvent.getStartTime().isBefore(now)) {
                throw new RouteUpdateRestrictedException(
                        format("Cannot update route, because events with id=%d has already ended, event start time = '%s",
                                currentEvent.getId(), currentEvent.getStartTime())
                );
            }

            if (i + 1 < events.size()) {
                EventEntity nextEvent = events.get(i + 1);
                if (currentEvent.getEndTime().isAfter(nextEvent.getStartTime()) ||
                        currentEvent.getEndTime().isEqual(nextEvent.getStartTime())) {
                    throw new RouteUpdateRestrictedException(
                            format("Cannot update route, because events overlap each other: " +
                                            "event with id=%d has start time = '%s' and end time = '%s', " +
                                            "event with id=%d has start time = '%s' and end time = '%s'",
                                    currentEvent.getId(), currentEvent.getStartTime(), currentEvent.getEndTime(),
                                    nextEvent.getId(), nextEvent.getStartTime(), nextEvent.getEndTime())
                    );
                }
            }
        }
    }
}
