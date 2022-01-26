package de.volkswagen.f73.evnavigator.model;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import javax.persistence.Id;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class Station {

    @Id
    @CsvBindByName(column = "id")
    private String id;
    @CsvBindByName(column = "name")
    private String name;
    @CsvCustomBindByName(column = "authentication_membership_card")
    private Boolean hasMembership;
    @CsvCustomBindByName(column = "fee")
    private Boolean hasFee;
    @CsvBindByName(column = "operator")
    private String operator;
    @CsvBindByName(column = "lon")
    private Double lon;
    @CsvBindByName(column = "lat")
    private Double lat;

    public Station() {
    }

    public Station(String id, String name, Boolean hasMembership, Boolean hasFee, Boolean operator, Double lon, Double lat) {
        this.id = id;
        this.name = name;
        this.hasMembership = hasMembership;
        this.hasFee = hasFee;
        this.operator = operator;
        this.lon = lon;
        this.lat = lat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasMembership() {
        return hasMembership;
    }

    public void setHasMembership(Boolean hasMembership) {
        this.hasMembership = hasMembership;
    }

    public Boolean getHasFee() {
        return hasFee;
    }

    public void setHasFee(Boolean hasFee) {
        this.hasFee = hasFee;
    }

    public Boolean getOperator() {
        return operator;
    }

    public void setOperator(Boolean operator) {
        this.operator = operator;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", hasMembership=" + hasMembership +
                ", hasFee=" + hasFee +
                ", operator=" + operator +
                ", lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
