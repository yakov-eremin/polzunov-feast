package com.example.polzunovfeastserver.place;

import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import com.example.polzunovfeastserver.place.excepition.PlaceNotFoundException;
import org.openapitools.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {
    private final PlaceEntityRepository placeEntityRepo;

    public PlaceService(PlaceEntityRepository placeRepo) {
        this.placeEntityRepo = placeRepo;
    }

    public Place addPlace(Place place) {
        place.setId(null);
        PlaceEntity placeEntity = PlaceMapper.toPlaceEntity(place);
        return PlaceMapper.toPlace(placeEntityRepo.save(placeEntity));
    }

    public Place updatePlaceById(Place place) {
        if (!placeEntityRepo.existsById(place.getId())) {
            throw new PlaceNotFoundException(
                    String.format("Cannot update place with id=%d, because place not found", place.getId())
            );
        }
        PlaceEntity placeEntity = PlaceMapper.toPlaceEntity(place);
        return PlaceMapper.toPlace(placeEntityRepo.save(placeEntity));
    }

    public List<Place> getAllPlaces(Integer page, Integer size) {
        Page<PlaceEntity> places = placeEntityRepo.findAll(PageRequest.of(page, size));
        return places.stream().map(PlaceMapper::toPlace).toList();
    }

    public Place getPlaceById(Long id) {
        PlaceEntity placeEntity = placeEntityRepo.findById(id).orElseThrow(() -> new PlaceNotFoundException(
                String.format("Cannot get place with id=%d, because place not found", id)
        ));
        return PlaceMapper.toPlace(placeEntity);
    }

    public void deletePlaceById(Long id) {
        if (!placeEntityRepo.existsById(id)) {
            return;
        }
        placeEntityRepo.deleteById(id);
    }
}
