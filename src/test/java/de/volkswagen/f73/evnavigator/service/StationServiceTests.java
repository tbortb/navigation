package de.volkswagen.f73.evnavigator.service;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.CoordinateLine;
import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.repository.StationRepository;
import org.junit.jupiter.api.Assertions;
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
@TestPropertySource(locations = "classpath:application-test.properties")
class StationServiceTests {

    @Autowired
    private StationService stationService;

    @Value("${stations.default.path}")
    private String stationsCsvLocation;

    @Autowired
    private StationRepository stationRepo;

    private final Set<Station> sampleStations = new HashSet<>();
    private final Set<Station> csvTestStaions = new HashSet<>();

    @BeforeEach
    void setUp() {
        this.stationRepo.deleteAll();
        this.sampleStations.clear();
        this.csvTestStaions.clear();
        this.csvTestStaions.add(new Station("node/663773225", null, null, true, "Allgäuer Überlandwerk", 47.727446, 10.3187933));
        this.csvTestStaions.add(new Station("node/5549505721", "name1", null, null, true, "3", false, null, null, "Tesla", null, null, null, 53.5167306, 12.6866268));
        //TODO:lat and long must be reverset in Stations
        this.csvTestStaions.add(new Station("node/7848608385", "name2", 36, 5, false, null, false, null, null, "Thüringer Energie AG", 0, 1, 22, 51.0150151, 11.0412338));
        this.sampleStations.add(new Station("MLC Station", false, false,
                "TestOperator", 52.42072813, 10.746537651));
        this.sampleStations.add(new Station("Kassel Station", false, false,
                "TestOperator", 51.31567287, 9.4978098202));
        this.sampleStations.add(new Station("Hannover Station", false, false,
                "TestOperator", 52.374206, 9.736088));
    }

    /**
     * Tests if a station that is close to a route gets returned
     */
    @Test
    void getStationsAlongPathShouldReturnCloseStationTest() {
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
    void getStationsAlongPathShouldReturnNoStationsTest() {
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
    void getStationsAlongPathShouldReturnAllStationsTest() {
        this.stationService.saveStations(this.sampleStations);

        Coordinate wolfsburg = new Coordinate(52.4204788, 10.74645087);
        Coordinate braunschweig = new Coordinate(52.26481063, 10.51790246);

        CoordinateLine wolfsburgToBraunschweig = new CoordinateLine(wolfsburg, braunschweig);
        List<Station> closeStations = this.stationService.getStationsAlongPath(wolfsburgToBraunschweig, 500.0);
        Assertions.assertEquals(3, closeStations.size());
    }

    /**
     * Tests whether stations along a linear path are found.
     * Wittenberge and Bad Karlshafen are connected with a line that crosses closely the MLC charging station.
     */
    @Test
    void getStationsAlongLineShouldReturnCloseStationTest() {
        this.stationService.saveStations(this.sampleStations);

        Coordinate wittenberge = new Coordinate(52.994915, 11.741919);
        Coordinate badKarlshafen = new Coordinate(51.643994, 9.438850);

        List<Station> actual = this.stationService.getStationsAlongLine(wittenberge, badKarlshafen, 3.0);

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals("MLC Station", actual.get(0).getName());

    }

    /**
     * Tests whether stations closely behind or after a linear path are found.
     * The route in this test starts a few kilometers away from each station. The line does not cross it,
     * but it is still close enough.
     */
    @Test
    void getStationsAlongLineShouldReturnStationsCloseToOriginAndDestinationTest() {
        this.stationService.saveStations(this.sampleStations);

        Coordinate kassel = new Coordinate(51.317938, 9.500416);
        Coordinate wolfsburg = new Coordinate(52.413913, 10.738788);

        List<Station> actual = this.stationService.getStationsAlongLine(kassel, wolfsburg, 5.0);

        Assertions.assertEquals(2, actual.size());
    }

    @Test
    void getStationAtCoordinateShouldReturnKnownStationTest() {
        this.stationService.saveStations(this.sampleStations);

        Station actual = this.stationService.getStationAtCoordinate(51.31567287, 9.4978098202);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Kassel Station", actual.getName());
    }

    /**
     * Tests if the conversion from csv to java beans works as expected
     */
    @Test
    void parseCsvToStationsTest() {
        List<Station> readStations = Assertions.assertDoesNotThrow(() -> this.stationService.csvToStations(this.stationsCsvLocation));

        Assertions.assertEquals(this.csvTestStaions.size(), readStations.size());
        readStations.forEach(st -> this.csvTestStaions.contains(st));
    }

    /**
     * Tests whether the parsed stations are correctly inserted into the database
     */
    @Test
    void insertStationsIntoDBTest() {
        List<Station> returnedStations = Assertions.assertDoesNotThrow(() -> this.stationService.saveStations(this.csvTestStaions));
        List<Station> insertedStations = this.stationRepo.findAll();

        //Compare returnedStations with sampleStations and compare insertedStations with sampleStations
        returnedStations.forEach(s -> Assertions.assertTrue(this.csvTestStaions.contains(s)));
        insertedStations.forEach(s -> Assertions.assertTrue(this.csvTestStaions.contains(s)));
    }

}
