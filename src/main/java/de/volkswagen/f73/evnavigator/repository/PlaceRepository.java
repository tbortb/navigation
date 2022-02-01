package de.volkswagen.f73.evnavigator.repository;

import de.volkswagen.f73.evnavigator.model.IPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for the Place model
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Repository
public interface PlaceRepository extends JpaRepository<IPlace, String> {
}
