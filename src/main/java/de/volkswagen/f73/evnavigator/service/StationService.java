package de.volkswagen.f73.evnavigator.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.CoordinateLine;
import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.repository.StationRepository;
import de.volkswagen.f73.evnavigator.util.GeoUtils;
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
 * Service class for the handling of station info
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Service
public class StationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StationService.class);

    @Autowired
    private StationRepository stationRepo;

    /**
     * Fetches all stations from DB.
     *
     * @return a List of Station objects
     */
    public List<Station> getAllStations() {
        return this.stationRepo.findAll();
    }

    /**
     * Saves a list of stations to DB.
     *
     * @param stations List of Station objects to save
     * @return  List of saved Station object
     */
    public List<Station> saveStations(Collection<Station> stations) {
        return this.stationRepo.saveAll(stations);
    }

    /**
     * Saves a station to DB.
     *
     * @param station a Station object to save
     * @return the saved Station object
     */
    public Station saveStation(Station station) {
        return this.stationRepo.save(station);
    }


    /**
     * Checks whether the database is already filled, if not, continues to save it.
     *
     * @param path filename of the CSV data to check
     */
    public void csvToEmptyDB(String path) {
        if (this.stationRepo.count() == 0) {
            this.csvToDB(path);
            LOGGER.debug("Filled database with default entries");
        } else {
            LOGGER.debug("Database is not empty");
        }
    }

    /**
     * Saves a CSV file of stations to DB.
     *
     * @param path filename of the CSV data to save
     * @return the saved list of Station objects
     */
    public List<Station> csvToDB(String path) {
        List<Station> stations = this.csvToStations(path);
        return this.saveStations(stations);
    }

    /**
     * Parses a CSV file and converts them to Station objects.
     *
     * @param path filename of the CSV data to parse
     * @return the parsed list of Station objects
     */
    public List<Station> csvToStations(String path) {

        List<Station> station = new ArrayList<>();
        try (
                Reader reader = Files.newBufferedReader(Paths.get(path))
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

    /**
     * Fetches stations within a radius of a given coordinate.
     *
     * @param lat       latitude
     * @param lon       longitude
     * @param maxDistKm maximum radius in Km
     * @return a List of Station objects within the defined radius
     */
    public List<Station> getStationsCloseTo(Double lat, Double lon, Double maxDistKm) {
        List<Station> stations = this.stationRepo.findAll();

        return stations.stream().parallel().filter(s -> GeoUtils.getLinearDistanceKm(lat, lon, s.getLat(), s.getLon()) <= maxDistKm).collect(Collectors.toList());
    }

    /**
     * Fetches a single station at a coordinate
     *
     * @param lat latitude as double
     * @param lon longitude as double
     * @return Station object at coordinate
     */
    public Station getStationAtCoordinate(Double lat, Double lon) {
        return this.stationRepo.findStationByLatEqualsAndLonEquals(lat, lon);
    }

    /**
     * Fetches stations within a distance of a given path.
     * Checks radii around waypoints along the route within max/min latitude/longitude boundaries.
     * To fill the potential gap around the start and destination coordinate, it adds another radius
     * around each of those.
     *
     * @param path      CoordinateLine containing at least two Coordinate objects
     * @param maxDistKm maximum distance in Km from the path
     * @return Station objects along the path
     */
    public List<Station> getStationsAlongPath(CoordinateLine path, Double maxDistKm) {

        double latMin = path.getCoordinateStream().mapToDouble(Coordinate::getLatitude).min().getAsDouble();
        double latMax = path.getCoordinateStream().mapToDouble(Coordinate::getLatitude).max().getAsDouble();
        double lonMin = path.getCoordinateStream().mapToDouble(Coordinate::getLongitude).min().getAsDouble();
        double lonMax = path.getCoordinateStream().mapToDouble(Coordinate::getLongitude).max().getAsDouble();

        List<Station> stationsWithinBoundaries = this.stationRepo.
                findByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(latMin, lonMin, latMax, lonMax);

        List<Station> filteredList = stationsWithinBoundaries.stream().parallel().filter(s ->
                path.getCoordinateStream().parallel().anyMatch(p -> GeoUtils.getLinearDistanceKm(
                        p.getLatitude(), p.getLongitude(), s.getLat(), s.getLon()) <= maxDistKm)
        ).collect(Collectors.toList());

        Coordinate firstCoord = path.getCoordinateStream().findFirst().orElse(null);
        Coordinate lastCoord = path.getCoordinateStream().reduce((first, second) -> second).orElse(null);

        if (firstCoord != null) {
            getStationsCloseTo(firstCoord.getLatitude(), firstCoord.getLongitude(), maxDistKm).forEach(st -> {
                if (!filteredList.contains(st)) {
                    filteredList.add(st);
                }
            });
        }

        if (lastCoord != null) {
            getStationsCloseTo(lastCoord.getLatitude(), lastCoord.getLongitude(), maxDistKm).forEach(st -> {
                if (!filteredList.contains(st)) {
                    filteredList.add(st);
                }
            });
        }

        return filteredList;
    }

    /**
     * Fetches stations within a distance of a line between two coordinates.
     * Checks radii around waypoints along the route within max/min latitude/longitude boundaries.
     * To fill the potential gap around the start and destination coordinate, it adds another radius
     * around each of those.
     *
     * @param path      CoordinateLine containing at least two Coordinate objects
     * @param maxDistKm maximum distance in Km from the path
     * @return Station objects along the path
     */
    public List<Station> getStationsAlongLine(Coordinate origin, Coordinate dest, Double maxDistKm) {

        double latMin = Math.min(origin.getLatitude(), dest.getLatitude());
        double latMax = Math.max(origin.getLatitude(), dest.getLatitude());
        double lonMin = Math.min(origin.getLongitude(), dest.getLongitude());
        double lonMax = Math.max(origin.getLongitude(), dest.getLongitude());


        List<Station> stationsWithinBoundaries = this.stationRepo.
                findByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(latMin, lonMin, latMax, lonMax);

        List<Station> filteredList = stationsWithinBoundaries
                .stream()
                .parallel()
                .filter(s -> Math.abs(GeoUtils.crossTrackDistanceFromLinearPathInKm(origin, dest,
                        new Coordinate(s.getLat(), s.getLon()))) <= maxDistKm)
                .collect(Collectors.toList());


        getStationsCloseTo(origin.getLatitude(), origin.getLongitude(), maxDistKm).forEach(st -> {
            if (!filteredList.contains(st)) {
                filteredList.add(st);
            }
        });

        getStationsCloseTo(dest.getLatitude(), dest.getLongitude(), maxDistKm).forEach(st -> {
            if (!filteredList.contains(st)) {
                filteredList.add(st);
            }
        });

        return filteredList;
    }
}
