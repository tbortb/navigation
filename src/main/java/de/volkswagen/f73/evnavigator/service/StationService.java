package de.volkswagen.f73.evnavigator.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Service
public class StationService {

    @Autowired
    private StationRepository stationRepo;

    public List<Station> getAllStations() {
        return this.stationRepo.findAll();
    }

    public List<Station> insertStationsToDB(Collection<Station> stations){
        return this.stationRepo.saveAll(stations);
    }

    public Station insertStationToDB(Station station){
        return this.stationRepo.save(station);
    }

    public List<Station> csvToDB(String path){
        List<Station> stations = csvToStations(path);
        return this.insertStationsToDB(stations);
    }

    public List<Station> csvToStations(String path) {

        List<Station> station = new ArrayList<>();
        try (
                Reader reader = Files.newBufferedReader(Paths.get(path));
        ) {
            CsvToBean<Station> csvToBean = new CsvToBeanBuilder<Station>(reader)
                    .withType(Station.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreQuotations(false)
                    .withSeparator(',')
                    .build();

            station = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return station;
    }
}
