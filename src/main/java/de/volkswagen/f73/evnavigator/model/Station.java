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
import java.util.stream.Stream;

/**
 * Model for station objects
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Entity
public class Station implements IPlace {

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

    private Station(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.maxVoltage = builder.maxVoltage;
        this.maxAmperage = builder.maxAmperage;
        this.hasMembership = builder.hasMembership;
        this.capacity = builder.capacity;
        this.hasFee = builder.hasFee;
        this.note = builder.note;
        this.openingHours = builder.openingHours;
        this.operator = builder.operator;
        this.socketSchukoAmount = builder.socketSchukoAmount;
        this.socketType2Amount = builder.socketType2Amount;
        this.socketType2Output = builder.socketType2Output;
        this.lat = builder.lat;
        this.lon = builder.lon;
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
        return this.maxVoltage;
    }

    public void setMaxVoltage(Integer maxVoltage) {
        this.maxVoltage = maxVoltage;
    }

    public Integer getMaxAmperage() {
        return this.maxAmperage;
    }

    public void setMaxAmperage(Integer maxAmperage) {
        this.maxAmperage = maxAmperage;
    }

    public String getCapacity() {
        return this.capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOpeningHours() {
        return this.openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public Integer getSocketSchukoAmount() {
        return this.socketSchukoAmount;
    }

    public void setSocketSchukoAmount(Integer socketSchukoAmount) {
        this.socketSchukoAmount = socketSchukoAmount;
    }

    public Integer getSocketType2Amount() {
        return this.socketType2Amount;
    }

    public void setSocketType2Amount(Integer socketType2Amount) {
        this.socketType2Amount = socketType2Amount;
    }

    public Integer getSocketType2Output() {
        return this.socketType2Output;
    }

    public void setSocketType2Output(Integer socketType2Output) {
        this.socketType2Output = socketType2Output;
    }

    @Override
    public String toString() {
        String output = Stream.of(this.name, this.operator)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(this.id);

        if (output.equals(this.operator)) {
            return output + " " + this.id.substring(5);
        }

        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(this.id, station.id) && Objects.equals(this.name, station.name)
                && Objects.equals(this.maxVoltage, station.maxVoltage)
                && Objects.equals(this.maxAmperage, station.maxAmperage)
                && Objects.equals(this.hasMembership, station.hasMembership)
                && Objects.equals(this.capacity, station.capacity)
                && Objects.equals(this.hasFee, station.hasFee)
                && Objects.equals(this.note, station.note)
                && Objects.equals(this.openingHours, station.openingHours)
                && Objects.equals(this.operator, station.operator)
                && Objects.equals(this.socketSchukoAmount, station.socketSchukoAmount)
                && Objects.equals(this.socketType2Amount, station.socketType2Amount)
                && Objects.equals(this.socketType2Output, station.socketType2Output)
                && Objects.equals(this.lon, station.lon)
                && Objects.equals(this.lat, station.lat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.maxVoltage, this.maxAmperage, this.hasMembership,
                this.capacity, this.hasFee, this.note, this.openingHours, this.operator, this.socketSchukoAmount,
                this.socketType2Amount, this.socketType2Output, this.lon, this.lat);
    }

    /**
     * Builder class for creating Station objects
     */
    public static class Builder {
        private String id;
        private String name;
        private Integer maxVoltage;
        private Integer maxAmperage;
        private Boolean hasMembership;
        private String capacity;
        private Boolean hasFee;
        private String note;
        private String openingHours;
        private String operator;
        private Integer socketSchukoAmount;
        private Integer socketType2Amount;
        private Integer socketType2Output;
        private Double lat;
        private Double lon;

        public Station build() {
            return new Station(this);
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withMaxVoltage(Integer maxVoltage) {
            this.maxVoltage = maxVoltage;
            return this;
        }

        public Builder withMaxAmperage(Integer maxAmperage) {
            this.maxAmperage = maxAmperage;
            return this;
        }

        public Builder withHasMembership(Boolean hasMembership) {
            this.hasMembership = hasMembership;
            return this;
        }

        public Builder withCapacity(String capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder withHasFee(Boolean hasFee) {
            this.hasFee = hasFee;
            return this;
        }

        public Builder withNote(String note) {
            this.note = note;
            return this;
        }

        public Builder withOpeningHours(String openingHours) {
            this.openingHours = openingHours;
            return this;
        }

        public Builder withOperator(String operator) {
            this.operator = operator;
            return this;
        }

        public Builder withSocketSchukoAmount(Integer socketSchukoAmount) {
            this.socketSchukoAmount = socketSchukoAmount;
            return this;
        }

        public Builder withSocketType2Amount(Integer socketType2Amount) {
            this.socketType2Amount = socketType2Amount;
            return this;
        }

        public Builder withSocketType2Output(Integer socketType2Output) {
            this.socketType2Output = socketType2Output;
            return this;
        }

        public Builder withLat(Double lat) {
            this.lat = lat;
            return this;
        }

        public Builder withLon(Double lon) {
            this.lon = lon;
            return this;
        }
    }
}
