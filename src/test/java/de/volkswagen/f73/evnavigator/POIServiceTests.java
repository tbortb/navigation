package de.volkswagen.f73.evnavigator;

import de.volkswagen.f73.evnavigator.model.POI;
import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.repository.POIRepository;
import de.volkswagen.f73.evnavigator.service.POIService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@SpringBootTest
@TestPropertySource(locations= "classpath:application-test.properties")
public class POIServiceTests {

    @Autowired
    private POIService poiService;

    @Autowired
    private POIRepository poiRepo;

    private List<POI> samplePOIs = new ArrayList<>();

    @BeforeEach
    void setUp(){
        this.poiRepo.deleteAll();
        this.samplePOIs.clear();
        this.samplePOIs.add(new POI(null, "first", 49.243824, -121.887340));
        this.samplePOIs.add(new POI(null, "second", 49.235347, -121.92532));
    }

    /**
     * Check if a POI can be added to the POIRepository
     */
    @Test
    void addPOITest(){
        POI toSave = this.samplePOIs.get(0);
        POI saved = Assertions.assertDoesNotThrow(() -> this.poiService.savePOI(toSave));

        Assertions.assertNotNull(saved);
        Assertions.assertEquals(toSave.getName(), saved.getName());
        Assertions.assertNotNull(saved.getId());
    }

    /**
     * Check if POIs can be fetched from the POIRepository
     */
    @Test
    void getPOITest(){
        for (POI poi : this.samplePOIs){
            Assertions.assertDoesNotThrow(() -> this.poiService.savePOI(poi));
        }

        List<POI> fetched = Assertions.assertDoesNotThrow(() -> this.poiService.getAllPOIs());

        Assertions.assertNotNull(fetched);
        Assertions.assertEquals(this.samplePOIs.size(), fetched.size());

        fetched.forEach(f -> Assertions.assertNotNull(f.getId()));

    }

    /**
     * Check if the POIService correctly fetches pois close to a given point
     */
    @Test
    void getClosePOIsTest(){
        double closeToLat = 49.243858;
        double closeToLon = -121.887360;
        double maxDistKm = 1;
        int expectedSize = 1;

        for (POI poi : this.samplePOIs){
            Assertions.assertDoesNotThrow(() -> this.poiService.savePOI(poi));
        }

        List<POI> fetched = Assertions.assertDoesNotThrow(() -> this.poiService.getPOIsCloseTo(closeToLat, closeToLon, maxDistKm));

        Assertions.assertNotNull(fetched);
        Assertions.assertEquals(expectedSize, fetched.size());

        fetched.forEach(f -> Assertions.assertNotNull(f.getId()));
    }


}
