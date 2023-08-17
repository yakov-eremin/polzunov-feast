package com.example.polzunovfeastserver.event;

import com.example.polzunovfeastserver.category.CategoryEntity;
import com.example.polzunovfeastserver.category.CategoryMapper;
import com.example.polzunovfeastserver.category.CategoryService;
import com.example.polzunovfeastserver.event.exception.EventAlreadyStartedException;
import com.example.polzunovfeastserver.event.exception.EventHasAssociatedRoutesException;
import com.example.polzunovfeastserver.event.exception.EventNotFoundException;
import com.example.polzunovfeastserver.image.ImageEntity;
import com.example.polzunovfeastserver.image.ImageService;
import com.example.polzunovfeastserver.image.exception.ImageUrlNotFoundException;
import com.example.polzunovfeastserver.place.PlaceEntity;
import com.example.polzunovfeastserver.place.PlaceService;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.polzunovfeastserver.event.EventMapper.toEventEntity;
import static com.example.polzunovfeastserver.event.EventMapper.toEventWithPlaceResponse;
import static com.example.polzunovfeastserver.event.util.EventEntitySpecifications.where;
import static com.example.polzunovfeastserver.image.ImageMapper.toImageUrls;
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
    private final ImageService imageService;

    /**
     * @throws PlaceNotFoundException place not found
     */
    public EventWithPlaceResponse addEvent(Event event) {
        event.setId(null);
        event.setCanceled(false);

        PlaceEntity placeEntity = placeService.getEntityById(event.getPlaceId());
        Set<CategoryEntity> categoryEntities = categoryService.getAllEntitiesById(event.getCategoryIds());
        EventEntity eventEntity = eventRepo.save(toEventEntity(event, placeEntity, categoryEntities, null, new HashSet<>()));

        Set<Category> categories = eventEntity.getCategories().stream().map(CategoryMapper::toCategory).collect(toSet());
        return toEventWithPlaceResponse(eventEntity, toPlace(placeEntity), categories, new HashSet<>());
    }

    /**
     * @throws EventNotFoundException                                                    event not found
     * @throws PlaceNotFoundException                                                    place not found
     * @throws EventAlreadyStartedException                                              It's already started, or it's in someone's route.
     *                                                                                   This checks will not be performed if event is canceled
     * @throws com.example.polzunovfeastserver.image.exception.ImageUrlNotFoundException some image urls were not found
     */
    public EventWithPlaceResponse updateEventById(Event event) {
        EventEntity eventEntity = getEntityById(event.getId());

        checkThatCanBeUpdated(eventEntity, event);
        updateEventImages(eventEntity, event);

        //We save 'canceled' before update because we need to send notifications only after data was saved.
        boolean entityCanceled = eventEntity.isCanceled();

        Set<CategoryEntity> categoryEntities = categoryService.getAllEntitiesById(event.getCategoryIds());
        eventEntity = eventRepo.save(
                toEventEntity(event,
                        placeService.getEntityById(event.getPlaceId()),
                        categoryEntities,
                        eventEntity.getMainImage(),
                        eventEntity.getImages())
        );

        if (!entityCanceled && event.getCanceled()) {
            //TODO send notification that event is canceled
            log.info("Sending notification that event with id={} is canceled", event.getId());
        } else if (entityCanceled && !event.getCanceled()) {
            //TODO send notification that event is not canceled again
            log.info("Sending notification that event with id={} is not canceled again", event.getId());
        }

        Place place = toPlace(eventEntity.getPlace());
        Set<Category> categories = eventEntity.getCategories().stream().map(CategoryMapper::toCategory).collect(toSet());
        Set<String> imageUrls = toImageUrls(eventEntity.getMainImage(), eventEntity.getImages());
        return toEventWithPlaceResponse(eventEntity, place, categories, imageUrls);
    }

    /**
     * @throws com.example.polzunovfeastserver.image.exception.ImageUrlNotFoundException    some image urls were not found
     * @throws com.example.polzunovfeastserver.image.exception.FailedToDeleteImageException cannot delete some images from file system
     */
    private void updateEventImages(EventEntity currEvent, Event newEvent) {
        if (newEvent.getImageUrls().isEmpty()) {
            deleteOldEventImagesFromFileSystem(currEvent, new HashSet<>());
            currEvent.setMainImage(null);
            currEvent.getImages().clear();
            return;
        }

        Set<ImageEntity> newImageEntities = imageService.findAllEntitiesByUrls(newEvent.getImageUrls()); //find all new images
        deleteOldEventImagesFromFileSystem(currEvent, newImageEntities); //delete all images that are not in a new image set

        //find main image (the first image in the provided set)
        String mainImageUrl = newEvent.getImageUrls().iterator().next();
        ImageEntity mainImageEntity = extractMainImage(newImageEntities, mainImageUrl);

        currEvent.setMainImage(mainImageEntity);
        currEvent.getImages().clear();
        currEvent.getImages().addAll(newImageEntities);
    }


    /**
     * Finds imageEntity with specified url, removes this entity from imageEntities and returns it.
     *
     * @throws ImageUrlNotFoundException couldn't find image entity with such url
     */
    private ImageEntity extractMainImage(Set<ImageEntity> imageEntities, String mainImageUrl) {
        ImageEntity mainImageEntity = null;

        for (ImageEntity newImageEntity : imageEntities) {
            if (newImageEntity.getUrl().equals(mainImageUrl)) {
                mainImageEntity = newImageEntity;
                break;
            }
        }
        if (mainImageEntity == null) {
            throw new ImageUrlNotFoundException("Main image url not found: " + mainImageUrl);
        }
        imageEntities.remove(mainImageEntity);
        return mainImageEntity;
    }

    /**
     * Deletes images that are not in a new image set from the file system
     *
     * @throws com.example.polzunovfeastserver.image.exception.FailedToDeleteImageException cannot delete some images
     */
    private void deleteOldEventImagesFromFileSystem(EventEntity currEvent, Set<ImageEntity> newImageEntities) {
        Set<ImageEntity> prevImageEntities = new HashSet<>(currEvent.getImages());
        if (currEvent.getMainImage() != null) {
            prevImageEntities.add(currEvent.getMainImage());
        }
        prevImageEntities.removeAll(newImageEntities);
        if (!prevImageEntities.isEmpty()) {
            imageService.deleteImagesFromFileSystem(prevImageEntities);
        }
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
     * @throws EventAlreadyStartedException      it's already started
     * @throws EventHasAssociatedRoutesException it's in someone's route
     */
    private void checkThatCanBeUpdated(EventEntity currentEvent, Event newEvent) {
        if (currentEvent.isCanceled()) {
            return;
        }

        //check if it's already started
        OffsetDateTime now = OffsetDateTime.now();
        if (newEvent.getStartTime().isBefore(now) || newEvent.getStartTime().isEqual(now)) {
            throw new EventAlreadyStartedException(
                    format("Cannot update event with id=%d, because it's already started, " +
                            "event start time = '%s'", newEvent.getId(), newEvent.getStartTime()));
        }

        //check if it's in someone's route
        if (nodeRepo.existsByEventId(newEvent.getId())) {
            throw new EventHasAssociatedRoutesException(
                    format("Cannot update event with id=%d, because it's in someone's route.", newEvent.getId()));
        }
    }

    public List<EventWithPlaceResponse> getAllEvents(EventParameter eventParameter, Integer page, Integer size) {


        Page<EventEntity> events = eventRepo.findAll(where(eventParameter), PageRequest.of(page, size));
        return events.stream().map(ev ->
                toEventWithPlaceResponse(
                        ev,
                        toPlace(ev.getPlace()),
                        ev.getCategories().stream().map(CategoryMapper::toCategory).collect(toSet()),
                        toImageUrls(ev.getMainImage(), ev.getImages())
                )
        ).toList();
    }

    /**
     * @throws EventNotFoundException event with this id not found
     */
    public EventWithPlaceResponse getEventById(Long id) {
        EventEntity eventEntity = getEntityById(id);
        return toEventWithPlaceResponse(
                eventEntity,
                toPlace(eventEntity.getPlace()),
                eventEntity.getCategories().stream().map(CategoryMapper::toCategory).collect(toSet()),
                toImageUrls(eventEntity.getMainImage(), eventEntity.getImages())
        );
    }

    public void deleteEventById(Long id) {
        if (!eventRepo.existsById(id)) {
            return;
        }
        eventRepo.deleteById(id);
    }

    /**
     * @param main specify if this image is main or not
     * @throws EventNotFoundException                                                     event with this id is not found
     * @throws UnsupportedOperationException                                              content type of provided file is null or unsupported
     * @throws com.example.polzunovfeastserver.image.exception.FailedToSaveImageException cannot save image
     */
    public String addEventImage(Long id, MultipartFile image, Boolean main) {
        EventEntity eventEntity = getEntityById(id);
        ImageEntity imageEntity = imageService.addImage(image);
        if (main) {
            if (eventEntity.getMainImage() != null) {
                eventEntity.getImages().add(eventEntity.getMainImage());
            }
            eventEntity.setMainImage(imageEntity);
        } else {
            eventEntity.getImages().add(imageEntity);
        }
        eventRepo.save(eventEntity);
        return imageEntity.getUrl();
    }

    /**
     * @throws EventNotFoundException event with this id not found
     */
    public EventEntity getEntityById(Long id) {
        return eventRepo.findById(id).orElseThrow(() -> new EventNotFoundException(
                format("Cannot get event with id=%d, because event not found", id)
        ));
    }
}
