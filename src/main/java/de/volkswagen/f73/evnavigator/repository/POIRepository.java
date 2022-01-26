package de.volkswagen.f73.evnavigator.repository;

import de.volkswagen.f73.evnavigator.model.POI;
import de.volkswagen.f73.evnavigator.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Repository
public interface POIRepository extends JpaRepository<POI, String> {
}
