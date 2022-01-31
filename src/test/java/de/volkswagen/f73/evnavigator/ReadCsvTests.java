package de.volkswagen.f73.evnavigator;

import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.repository.StationRepository;
import de.volkswagen.f73.evnavigator.service.StationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ReadCsvTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadCsvTests.class);

    @Value("${stations.default.path}")
    private String stationsCsvLocation;

    @Autowired
    private StationService stationService;

    @Autowired
    private StationRepository stationRepo;

    private final Set<Station> sampleStations = new HashSet<>();

    @BeforeEach
    void setUp(){
        this.stationRepo.deleteAll();
        this.sampleStations.clear();
        this.sampleStations.add(new Station("node/663773225", null, null, true, "Allgäuer Überlandwerk", 47.727446, 10.3187933));
        this.sampleStations.add(new Station("node/5549505721", "name1", null, null, true, "3", false, null, null, "Tesla", null, null, null, 53.5167306, 12.6866268));
        //TODO:lat and long must be reverset in Stations
        this.sampleStations.add(new Station("node/7848608385", "name2", 36, 5, false,null, false,null, null, "Thüringer Energie AG",0,1,22,51.0150151 ,11.0412338));
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

        Assertions.assertEquals(this.sampleStations.size(), readStations.size());

        this.sampleStations.forEach(s -> LOGGER.info(s.toString()));

        for (Station readStation : readStations){
            LOGGER.info(readStation.toString());
            Assertions.assertTrue(this.sampleStations.contains(readStation));

        }
    }

    /**
     * Tests whether the parsed stations are correctly inserted into the database
     */
    @Test
    void insertStationsIntoDBTest(){
        List<Station> returnedStations = Assertions.assertDoesNotThrow(() -> this.stationService.insertStationsToDB(this.sampleStations));
        List<Station> insertedStations = this.stationRepo.findAll();

        //Compare returnedStations with sampleStations and compare insertedStations with sampleStations
        returnedStations.forEach(s -> Assertions.assertTrue(this.sampleStations.contains(s)));
        insertedStations.forEach(s -> Assertions.assertTrue(this.sampleStations.contains(s)));
    }
}