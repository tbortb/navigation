package de.volkswagen.f73.evnavigator.repository;

import de.volkswagen.f73.evnavigator.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for the Station model
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Repository
public interface StationRepository extends JpaRepository<Station, String> {
    List<Station> findByLatGreaterThanAndLonGreaterThanAndLatLessThanAndLonLessThan(Double lat, Double lon, Double lat2, Double lon2);
}
