package de.volkswagen.f73.evnavigator.model.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import de.volkswagen.f73.evnavigator.model.Station;

import java.util.OptionalInt;
import java.util.stream.Stream;

/**
 * OpenCSV helper class for parsing CSV fields
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class MaxIntConverter extends AbstractBeanField<Station, Integer> {
    /**
     * parse the largest integer from the textinput
     *
     * @param checkString is the string to be parsed
     * @return the largest integer for the string or null if there is no integer
     * @throws CsvDataTypeMismatchException
     * @throws CsvConstraintViolationException
     */
    @Override
    public Integer convert(String checkString) throws CsvDataTypeMismatchException, CsvConstraintViolationException {

        String[] parts = checkString.split("[^\\d]");
        OptionalInt parsedInt = Stream.of(parts).filter(p -> p.length() > 0).mapToInt(Integer::parseInt).max();
        if (parsedInt.isPresent()) {
            return parsedInt.getAsInt();
        }
        return null;
    }
}
