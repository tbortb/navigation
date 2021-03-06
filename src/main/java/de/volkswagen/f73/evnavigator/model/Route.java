package de.volkswagen.f73.evnavigator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Model for route objects
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Double startLat;
    private Double startLon;
    private Double endLat;
    private Double endLon;

    public Route() {
    }

    public Route(Integer id, String name, Double startLat, Double startLon, Double endLat, Double endLon) {
        this.id = id;
        this.name = name;
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
    }

    public Route(String name, Double startLat, Double startLon, Double endLat, Double endLon) {
        this.name = name;
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getStartLat() {
        return this.startLat;
    }

    public void setStartLat(Double startLat) {
        this.startLat = startLat;
    }

    public Double getStartLon() {
        return this.startLon;
    }

    public void setStartLon(Double startLon) {
        this.startLon = startLon;
    }

    public Double getEndLat() {
        return this.endLat;
    }

    public void setEndLat(Double endLat) {
        this.endLat = endLat;
    }

    public Double getEndLon() {
        return this.endLon;
    }

    public void setEndLon(Double endLon) {
        this.endLon = endLon;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
