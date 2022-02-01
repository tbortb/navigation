package de.volkswagen.f73.evnavigator.service;

import de.volkswagen.f73.evnavigator.model.Place;
import de.volkswagen.f73.evnavigator.repository.PlaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@SpringBootTest
@TestPropertySource(locations= "classpath:application-test.properties")
class PlaceServiceTests {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private PlaceRepository placeRepo;

    private final List<Place> samplePlaces = new ArrayList<>();

    @BeforeEach
    void setUp(){
        this.placeRepo.deleteAll();
        this.samplePlaces.clear();
        this.samplePlaces.add(new Place(null, "first", 49.243824, -121.887340));
        this.samplePlaces.add(new Place(null, "second", 49.235347, -121.92532));
    }

    /**
     * Check if a Place can be added to the PlaceRepository
     */
    @Test
    void addPlaceTest(){
        Place toSave = this.samplePlaces.get(0);
        Place saved = Assertions.assertDoesNotThrow(() -> this.placeService.savePlace(toSave));

        Assertions.assertNotNull(saved);
        Assertions.assertEquals(toSave.getName(), saved.getName());
        Assertions.assertNotNull(saved.getId());
    }

    /**
     * Check if POIs can be fetched from the PlaceRepository
     */
    @Test
    void getPlaceTest(){
        for (Place place : this.samplePlaces){
            Assertions.assertDoesNotThrow(() -> this.placeService.savePlace(place));
        }

        List<Place> fetched = Assertions.assertDoesNotThrow(() -> this.placeService.getAllPlaces());

        Assertions.assertNotNull(fetched);
        Assertions.assertEquals(this.samplePlaces.size(), fetched.size());

        fetched.forEach(f -> Assertions.assertNotNull(f.getId()));

    }

    /**
     * Check if the PlaceService correctly fetches pois close to a given point
     */
    @Test
    void getClosePlacesTest(){
        double closeToLat = 49.243858;
        double closeToLon = -121.887360;
        double maxDistKm = 1;
        int expectedSize = 1;

        for (Place place : this.samplePlaces){
            Assertions.assertDoesNotThrow(() -> this.placeService.savePlace(place));
        }

        List<Place> fetched = Assertions.assertDoesNotThrow(() -> this.placeService.getPlacesCloseTo(closeToLat, closeToLon, maxDistKm));

        Assertions.assertNotNull(fetched);
        Assertions.assertEquals(expectedSize, fetched.size());

        fetched.forEach(f -> Assertions.assertNotNull(f.getId()));
    }

    /**
     * Check if a place gets deleted from DB
     */
    @Test
    void deletePlaceTest() {
        for (Place place : this.samplePlaces){
            this.placeService.savePlace(place);
        }

        this.placeService.deletePlace(this.samplePlaces.get(0));
        Assertions.assertFalse(this.placeService.getAllPlaces().contains(this.samplePlaces.get(0)));
        Assertions.assertEquals(1, this.placeService.getAllPlaces().size());
    }

}
