package de.volkswagen.f73.evnavigator.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import de.volkswagen.f73.evnavigator.util.IDGenerator;
import de.volkswagen.f73.evnavigator.util.csv_converters.FeeConverter;
import de.volkswagen.f73.evnavigator.util.csv_converters.MembershipConverter;
import de.volkswagen.f73.evnavigator.util.csv_converters.StringConverter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
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


    //TODO: Apply correct format
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) && Objects.equals(name, station.name) && Objects.equals(hasMembership, station.hasMembership) && Objects.equals(hasFee, station.hasFee) && Objects.equals(operator, station.operator) && Objects.equals(lon, station.lon) && Objects.equals(lat, station.lat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hasMembership, hasFee, operator, lon, lat);
    }
}
