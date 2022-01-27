package de.volkswagen.f73.evnavigator.service;

import de.volkswagen.f73.evnavigator.model.Place;
import de.volkswagen.f73.evnavigator.repository.PlaceRepository;
import de.volkswagen.f73.evnavigator.util.DistanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepo;

    public List<Place> getAllPlaces(){
        return this.placeRepo.findAll();
    }

    public Place savePlace(Place place){
        return this.placeRepo.save(place);
    }

    public List<Place> getPlacesCloseTo(Double lat, Double lon, Double maxDistKm){
        List<Place> places = this.placeRepo.findAll();

        return places.stream().filter(p -> DistanceCalculator.getLinearDistanceKm(lat, lon, p.getLat(), p.getLon()) <= maxDistKm).collect(Collectors.toList());
    }
}
