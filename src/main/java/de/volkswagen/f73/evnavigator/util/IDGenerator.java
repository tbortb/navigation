package de.volkswagen.f73.evnavigator.util;

import de.volkswagen.f73.evnavigator.model.Station;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

/**
 * Generates a random positive integer to use as a pseudounique ID.
 *
 * @author Bücker, Thies (SE-A/34)
 * @author Justo, David (SE-A/34)
 */
public class IDGenerator implements IdentifierGenerator {

    public static final String GENERATOR_NAME = "StationIDGenerator";

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object) throws HibernateException {

        Station bla = (Station) object;
        if (bla.getId() != null) {
            return bla.getId();
        }
        return UUID.randomUUID().toString();

    }
}
