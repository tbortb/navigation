package de.volkswagen.f73.evnavigator.model;

import org.springframework.stereotype.Component;

/**
 * Interface for objects that are defined by its latitude and longitude
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
public interface IPlace {

    String getName();

    void setName(String name);

    Double getLat();

    void setLat(Double lat);

    Double getLon();

    void setLon(Double lon);

}
