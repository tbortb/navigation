package de.volkswagen.f73.evnavigator.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import de.volkswagen.f73.evnavigator.model.csvconverters.*;
import de.volkswagen.f73.evnavigator.util.IDGenerator;
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
public class Station implements PlaceBase{

    @Id
    @GeneratedValue(generator = IDGenerator.GENERATOR_NAME)
    @GenericGenerator(name = IDGenerator.GENERATOR_NAME, strategy = "de.volkswagen.f73.evnavigator.util.IDGenerator")
    @CsvCustomBindByName(column = "id", converter = StringConverter.class)
    private String id;
    @CsvCustomBindByName(column = "name", converter = StringConverter.class)
    private String name;
    @CsvCustomBindByName(column = "voltage", converter = MaxIntConverter.class)
    private Integer maxVoltage;
    @CsvCustomBindByName(column = "amperage", converter = MaxIntConverter.class)
    private Integer maxAmperage;
    @CsvCustomBindByName(column = "authentication_membership_card", converter = MembershipConverter.class)
    private Boolean hasMembership;
    @CsvCustomBindByName(column = "capacity", converter = StringConverter.class)
    private String capacity;
    @CsvCustomBindByName(column = "fee", converter = FeeConverter.class)
    private Boolean hasFee;
    @CsvCustomBindByName(column = "note", converter = StringConverter.class)
    private String note;
    @CsvCustomBindByName(column = "opening_hours", converter = StringConverter.class)
    private String openingHours;
    @CsvCustomBindByName(column = "operator", converter = StringConverter.class)
    private String operator;
    @CsvCustomBindByName(column = "socket_schuko", converter = MaxIntConverter.class)
    private Integer socketSchukoAmount;
    @CsvCustomBindByName(column = "socket_type2", converter = MaxIntConverter.class)
    private Integer socketType2Amount;
    @CsvCustomBindByName(column = "socket_type2_output", converter = SocketType2Converter.class)
    private Integer socketType2Output;
    @CsvBindByName(column = "lon")
    private Double lon;
    @CsvBindByName(column = "lat")
    private Double lat;

    public Station() {
    }

    public Station(String id, String name, Integer maxVoltage, Integer maxAmperage, Boolean hasMembership, String capacity, Boolean hasFee, String note, String openingHours, String operator, Integer socketSchukoAmount, Integer socketType2Amount, Integer socketType2Output, Double lat, Double lon) {
        this.id = id;
        this.name = name;
        this.maxVoltage = maxVoltage;
        this.maxAmperage = maxAmperage;
        this.hasMembership = hasMembership;
        this.capacity = capacity;
        this.hasFee = hasFee;
        this.note = note;
        this.openingHours = openingHours;
        this.operator = operator;
        this.socketSchukoAmount = socketSchukoAmount;
        this.socketType2Amount = socketType2Amount;
        this.socketType2Output = socketType2Output;
        this.lon = lon;
        this.lat = lat;
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

    public Integer getMaxVoltage() {
        return maxVoltage;
    }

    public void setMaxVoltage(Integer maxVoltage) {
        this.maxVoltage = maxVoltage;
    }

    public Integer getMaxAmperage() {
        return maxAmperage;
    }

    public void setMaxAmperage(Integer maxAmperage) {
        this.maxAmperage = maxAmperage;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public Integer getSocketSchukoAmount() {
        return socketSchukoAmount;
    }

    public void setSocketSchukoAmount(Integer socketSchukoAmount) {
        this.socketSchukoAmount = socketSchukoAmount;
    }

    public Integer getSocketType2Amount() {
        return socketType2Amount;
    }

    public void setSocketType2Amount(Integer socketType2Amount) {
        this.socketType2Amount = socketType2Amount;
    }

    public Integer getSocketType2Output() {
        return socketType2Output;
    }

    public void setSocketType2Output(Integer socketType2Output) {
        this.socketType2Output = socketType2Output;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", maxVoltage=" + maxVoltage +
                ", maxAmperage=" + maxAmperage +
                ", hasMembership=" + hasMembership +
                ", capacity='" + capacity + '\'' +
                ", hasFee=" + hasFee +
                ", note='" + note + '\'' +
                ", openingHours='" + openingHours + '\'' +
                ", operator='" + operator + '\'' +
                ", socketSchukoAmount=" + socketSchukoAmount +
                ", socketType2Amount=" + socketType2Amount +
                ", socketType2Output=" + socketType2Output +
                ", lon=" + lon +
                ", lat=" + lat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) && Objects.equals(name, station.name) && Objects.equals(maxVoltage, station.maxVoltage) && Objects.equals(maxAmperage, station.maxAmperage) && Objects.equals(hasMembership, station.hasMembership) && Objects.equals(capacity, station.capacity) && Objects.equals(hasFee, station.hasFee) && Objects.equals(note, station.note) && Objects.equals(openingHours, station.openingHours) && Objects.equals(operator, station.operator) && Objects.equals(socketSchukoAmount, station.socketSchukoAmount) && Objects.equals(socketType2Amount, station.socketType2Amount) && Objects.equals(socketType2Output, station.socketType2Output) && Objects.equals(lon, station.lon) && Objects.equals(lat, station.lat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, maxVoltage, maxAmperage, hasMembership, capacity, hasFee, note, openingHours, operator, socketSchukoAmount, socketType2Amount, socketType2Output, lon, lat);
    }
}
