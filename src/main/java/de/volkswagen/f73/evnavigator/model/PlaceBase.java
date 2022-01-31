package de.volkswagen.f73.evnavigator.model;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Component
public interface PlaceBase {

    public String getName();

    public void setName(String name);

    public Double getLat();

    public void setLat(Double lat);

    public Double getLon();

    public void setLon(Double lon);

}
