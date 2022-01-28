package de.volkswagen.f73.evnavigator.service;

import de.volkswagen.f73.evnavigator.model.Place;
import de.volkswagen.f73.evnavigator.repository.PlaceRepository;
import de.volkswagen.f73.evnavigator.util.GeoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for the handling of place info
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepo;

    /**
     * Fetches all Place entries from database.
     *
     * @return List of Place objects
     */
    public List<Place> getAllPlaces() {
        return this.placeRepo.findAll();
    }

    /**
     * Saves a single Place to database.
     *
     * @param place the Place object to save
     * @return      the saved Place object
     */
    public Place savePlace(Place place) {
        return this.placeRepo.save(place);
    }

    /**
     * Returns a list of Places within a defined distance around a coordinate.
     *
     * @param lat       latitude
     * @param lon       longitude
     * @param maxDistKm maximum distance in kilometers
     * @return          a list of Places close to the coordinate
     */
    public List<Place> getPlacesCloseTo(Double lat, Double lon, Double maxDistKm) {
        List<Place> places = this.placeRepo.findAll();

        return places.stream().filter(p -> GeoUtils.getLinearDistanceKm(lat, lon, p.getLat(), p.getLon()) <= maxDistKm).collect(Collectors.toList());
    }

    public void deletePlace(Place place) {
        this.placeRepo.delete(place);
    }
}
