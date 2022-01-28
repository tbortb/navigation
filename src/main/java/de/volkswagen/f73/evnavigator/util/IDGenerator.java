package de.volkswagen.f73.evnavigator.util;

import de.volkswagen.f73.evnavigator.model.Station;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * Generates an ID conditionally. If the object already has an ID (e.g. from CSV import) it keeps it,
 * if not it gets assigned a UUID.
 *
 * @author Bücker, Thies (SE-A/34)
 * @author Justo, David (SE-A/34)
 */
public class IDGenerator implements IdentifierGenerator {

    public static final String GENERATOR_NAME = "StationIDGenerator";

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object) throws HibernateException {

        Station station = (Station) object;
        if (station.getId() != null) {
            return station.getId();
        }
        return UUID.randomUUID().toString();

    }
}
