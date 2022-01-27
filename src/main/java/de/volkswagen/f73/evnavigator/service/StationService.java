package de.volkswagen.f73.evnavigator.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.CoordinateLine;
import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.repository.StationRepository;
import de.volkswagen.f73.evnavigator.util.DistanceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Service
public class StationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StationService.class);

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

    public void csvToEmptyDB(String path){
        if (this.stationRepo.count() == 0){
            this.csvToDB(path);
            LOGGER.debug("Filled database with default entries");
        } else{
            LOGGER.debug("Database is not empty");
        }
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
            LOGGER.error("Csv parsing error: {}", e.getMessage());
        }
        return station;
    }

    public Station saveStation(Station station){
        return this.stationRepo.save(station);
    }

    public List<Station> getStationsCloseTo(Double lat, Double lon, Double maxDistKm){
        List<Station> stations = this.stationRepo.findAll();

        return stations.stream().filter(s -> DistanceCalculator.getLinearDistanceKm(lat, lon, s.getLat(), s.getLon()) <= maxDistKm).collect(Collectors.toList());
    }

    public List<Station> getStationsAlongPath(CoordinateLine path, Double maxDistKm){

        double latMin = path.getCoordinateStream().mapToDouble(Coordinate::getLatitude).min().getAsDouble();
        double latMax = path.getCoordinateStream().mapToDouble(Coordinate::getLatitude).max().getAsDouble();
        double lonMin = path.getCoordinateStream().mapToDouble(Coordinate::getLongitude).min().getAsDouble();
        double lonMax = path.getCoordinateStream().mapToDouble(Coordinate::getLongitude).max().getAsDouble();

        List<Station> stationsWithinBoundaries = this.stationRepo.findByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(latMin, lonMin,latMax, lonMax);

        return stationsWithinBoundaries.stream().filter(s ->
            path.getCoordinateStream().anyMatch(p -> DistanceCalculator.getLinearDistanceKm(p.getLatitude(), p.getLongitude(), s.getLat(), s.getLon()) <= maxDistKm)
        ).collect(Collectors.toList());
    }
}
