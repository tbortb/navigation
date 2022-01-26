package de.volkswagen.f73.evnavigator;

import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.service.StationImportService;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
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
public class ReadCsvTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadCsvTests.class);

    @Value("${stations.default.path}")
    private String stationsCsvLocation;

    @Autowired
    private StationImportService stationImportService;


    /**
     * Tests whether the application is able to connect to provided Csv file
     */
    @Test
    void loadDefaultStationsCsvOkTest(){
        Assertions.assertNotNull(this.stationsCsvLocation);
        File file = new File(this.stationsCsvLocation);
        Assertions.assertTrue(file.exists());
    }

    @Test
    void parseCsvToStationsTest(){
        Set<Station> expectedStations = new HashSet<>();
        expectedStations.add(new Station("node/663773225", null, null, true, "Allgäuer Überlandwerk", 10.3187933, 47.727446));
        expectedStations.add(new Station("node/5549505721", "name1", true, false, "Tesla", 12.6866268,53.5167306));
        expectedStations.add(new Station("node/7848608385", "name2", false, false, "Thüringer Energie AG", 11.0412338,51.0150151));

        List<Station> readStations = Assertions.assertDoesNotThrow(() -> this.stationImportService.csvToStations(this.stationsCsvLocation));

        Assertions.assertEquals(expectedStations.size(), readStations.size());

        for (Station readStation : readStations){
            LOGGER.info(readStation.toString());
            Assertions.assertTrue(expectedStations.contains(readStation));
        }

    }


}
