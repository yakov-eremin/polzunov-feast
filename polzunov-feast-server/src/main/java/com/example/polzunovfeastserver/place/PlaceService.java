package com.example.polzunovfeastserver.place;

import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import com.example.polzunovfeastserver.place.excepition.PlaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceEntityRepository placeRepo;

    public Place addPlace(Place place) {
        place.setId(null);
        PlaceEntity placeEntity = PlaceMapper.toPlaceEntity(place);
        return PlaceMapper.toPlace(placeRepo.save(placeEntity));
    }

    public Place updatePlaceById(Place place) {
        if (!placeRepo.existsById(place.getId())) {
            throw new PlaceNotFoundException(
                    String.format("Cannot update place with id=%d, because place not found", place.getId())
            );
        }
        PlaceEntity placeEntity = PlaceMapper.toPlaceEntity(place);
        return PlaceMapper.toPlace(placeRepo.save(placeEntity));
    }

    public List<Place> getAllPlaces(Integer page, Integer size) {
        Page<PlaceEntity> places = placeRepo.findAll(PageRequest.of(page, size));
        return places.stream().map(PlaceMapper::toPlace).toList();
    }

    public Place getPlaceById(Long id) {
        PlaceEntity placeEntity = getEntityById(id);
        return PlaceMapper.toPlace(placeEntity);
    }

    public void deletePlaceById(Long id) {
        if (!placeRepo.existsById(id)) {
            return;
        }
        placeRepo.deleteById(id);
    }

    /**
     * @throws PlaceNotFoundException place not found
     */
    public PlaceEntity getEntityById(Long id) {
        return placeRepo.findById(id).orElseThrow(() -> new PlaceNotFoundException(
                String.format("Cannot get place with id=%d, because place not found", id)
        ));
    }
}
