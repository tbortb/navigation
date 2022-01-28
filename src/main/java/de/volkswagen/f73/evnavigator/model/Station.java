package de.volkswagen.f73.evnavigator.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import de.volkswagen.f73.evnavigator.util.IDGenerator;
import de.volkswagen.f73.evnavigator.model.csvconverters.FeeConverter;
import de.volkswagen.f73.evnavigator.model.csvconverters.MembershipConverter;
import de.volkswagen.f73.evnavigator.model.csvconverters.StringConverter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Entity
public class Station {

    //TODO: maybe add Socket_schuko, Socket_type2, Socket_type2_output

    @Id
    @GeneratedValue(generator = IDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = IDGenerator.GENERATOR_NAME, strategy = "de.volkswagen.f73.evnavigator.util.IDGenerator")
    @CsvCustomBindByName(column = "id", converter = StringConverter.class)
    private String id;
    @CsvCustomBindByName(column = "name", converter = StringConverter.class)
    private String name;
    @CsvCustomBindByName(column = "authentication_membership_card", converter = MembershipConverter.class)
    private Boolean hasMembership;
    @CsvCustomBindByName(column = "fee", converter = FeeConverter.class)
    private Boolean hasFee;
    @CsvCustomBindByName(column = "operator", converter = StringConverter.class)
    private String operator;
    @CsvBindByName(column = "lon")
    private Double lon;
    @CsvBindByName(column = "lat")
    private Double lat;

    public Station() {
    }

    public Station(String id, String name, Boolean hasMembership, Boolean hasFee, String operator, Double lat, Double lon) {
        this.id = id;
        this.name = name;
        this.hasMembership = hasMembership;
        this.hasFee = hasFee;
        this.operator = operator;
        this.lon = lon;
        this.lat = lat;
    }

    public Station(String name, Boolean hasMembership, Boolean hasFee, String operator, Double lat, Double lon) {
        this.name = name;
        this.hasMembership = hasMembership;
        this.hasFee = hasFee;
        this.operator = operator;
        this.lon = lon;
        this.lat = lat;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasMembership() {
        return this.hasMembership;
    }

    public void setHasMembership(Boolean hasMembership) {
        this.hasMembership = hasMembership;
    }

    public Boolean getHasFee() {
        return this.hasFee;
    }

    public void setHasFee(Boolean hasFee) {
        this.hasFee = hasFee;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Double getLon() {
        return this.lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return this.lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id='" + this.id + '\'' +
                ", name='" + this.name + '\'' +
                ", hasMembership=" + this.hasMembership +
                ", hasFee=" + this.hasFee +
                ", operator=" + this.operator +
                ", lon=" + this.lon +
                ", lat=" + this.lat +
                '}';
    }


    //TODO: Apply correct format
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(this.id, station.id) && Objects.equals(this.name, station.name) && Objects.equals(this.hasMembership, station.hasMembership) && Objects.equals(this.hasFee, station.hasFee) && Objects.equals(this.operator, station.operator) && Objects.equals(this.lon, station.lon) && Objects.equals(this.lat, station.lat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.hasMembership, this.hasFee, this.operator, this.lon, this.lat);
    }
}
