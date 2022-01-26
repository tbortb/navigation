package de.volkswagen.f73.evnavigator.util.csv_converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import de.volkswagen.f73.evnavigator.model.Station;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class StringConverter extends AbstractBeanField<Station, String> {

    @Override
    public String convert(String checkString) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        return checkString.equals("null") ? null : checkString;
    }
}
