package de.volkswagen.f73.evnavigator.service;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.CoordinateLine;
import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.repository.StationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@SpringBootTest
@TestPropertySource(locations= "classpath:application-test.properties")
class StationServiceTests {

    @Autowired
    private StationService stationService;

    @Value("${stations.default.path}")
    private String stationsCsvLocation;

    @Autowired
    private StationRepository stationRepo;

    private final Set<Station> sampleStations = new HashSet<>();
    private static final Set<Station> CSV_TEST_STATIONS = new HashSet<>();

    @BeforeEach
    void setUp() {
        this.stationRepo.deleteAll();
        this.sampleStations.clear();
        this.sampleStations.add(new Station("MLC Station", false, false,
                "TestOperator", 52.42072813, 10.746537651));
        this.sampleStations.add(new Station("Kassel Station", false, false,
                "TestOperator", 51.31567287, 9.4978098202));

        CSV_TEST_STATIONS.add(new Station("node/663773225", null, null, true, "Allgäuer Überlandwerk", 47.727446, 10.3187933));
        CSV_TEST_STATIONS.add(new Station("node/5549505721", "name1", null, null, true, "3", false, null, null, "Tesla", null, null, null, 53.5167306, 12.6866268));
        //TODO:lat and long must be reverset in Stations
        CSV_TEST_STATIONS.add(new Station("node/7848608385", "name2", 36, 5, false,null, false,null, null, "Thüringer Energie AG",0,1,22,51.0150151 ,11.0412338));

    }

    /**
     * Tests if a station that is close to a route gets returned
     */
    @Test
    void getStationsAlongPathShouldReturnCloseStation() {
        this.stationService.saveStations(this.sampleStations);

        Coordinate wolfsburg = new Coordinate(52.4245294, 10.7528508);
        Coordinate braunschweig = new Coordinate(52.26481063, 10.51790246);

        CoordinateLine wolfsburgToBraunschweig = new CoordinateLine(wolfsburg, braunschweig);
        List<Station> closeStations = this.stationService.getStationsAlongPath(wolfsburgToBraunschweig, 3.0);
        Assertions.assertEquals(1, closeStations.size());
        Assertions.assertEquals("MLC Station", closeStations.get(0).getName());
    }

    /**
     * Tests if a path with no stations in proximity returns no stations
     */
    @Test
    void getStationsAlongPathShouldReturnNoStations() {
        this.stationService.saveStations(this.sampleStations);

        Coordinate salzgitter = new Coordinate(52.15314926, 10.324741450);
        Coordinate braunschweig = new Coordinate(52.26481063, 10.51790246);

        CoordinateLine salzgitterToBraunschweig = new CoordinateLine(salzgitter, braunschweig);
        List<Station> closeStations = this.stationService.getStationsAlongPath(salzgitterToBraunschweig, 3.0);
        Assertions.assertTrue(closeStations.isEmpty());
    }

    /**
     * Tests if a big radius returns all qualified stations
     */
    @Test
    void getStationsAlongPathShouldReturnAllStations() {
        this.stationService.saveStations(this.sampleStations);

        Coordinate wolfsburg = new Coordinate(52.4204788, 10.74645087);
        Coordinate braunschweig = new Coordinate(52.26481063, 10.51790246);

        CoordinateLine wolfsburgToBraunschweig = new CoordinateLine(wolfsburg, braunschweig);
        List<Station> closeStations = this.stationService.getStationsAlongPath(wolfsburgToBraunschweig, 500.0);
        Assertions.assertEquals(2, closeStations.size());
    }

    /**
     * Tests whether the application is able to connect to provided Csv file
     */
    @Test
    void loadDefaultStationsCsvOkTest(){
        Assertions.assertNotNull(this.stationsCsvLocation);
        File file = new File(this.stationsCsvLocation);
        Assertions.assertTrue(file.exists());
    }

    /**
     * Tests if the conversion from csv to java beans works as expected
     */
    @Test
    void parseCsvToStationsTest(){
        List<Station> readStations = Assertions.assertDoesNotThrow(() -> this.stationService.csvToStations(this.stationsCsvLocation));

        Assertions.assertEquals(CSV_TEST_STATIONS.size(), readStations.size());

        for (Station readStation : readStations){
            Assertions.assertTrue(CSV_TEST_STATIONS.contains(readStation));

        }
    }

    /**
     * Tests whether the parsed stations are correctly inserted into the database
     */
    @Test
    void insertStationsIntoDBTest(){
        List<Station> returnedStations = Assertions.assertDoesNotThrow(() -> this.stationService.saveStations(CSV_TEST_STATIONS));
        List<Station> insertedStations = this.stationRepo.findAll();

        //Compare returnedStations with sampleStations and compare insertedStations with sampleStations
        returnedStations.forEach(s -> Assertions.assertTrue(CSV_TEST_STATIONS.contains(s)));
        insertedStations.forEach(s -> Assertions.assertTrue(CSV_TEST_STATIONS.contains(s)));
    }

}
