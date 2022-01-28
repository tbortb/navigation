package de.volkswagen.f73.evnavigator.repository;

import de.volkswagen.f73.evnavigator.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for the Route model
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
}
