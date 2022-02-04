package de.volkswagen.f73.evnavigator.model.csvconverters;

import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class SocketType2Converter extends MaxIntConverter {

    @Override
    public Integer convert(String checkString) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        Integer maxInt = super.convert(checkString);
        if (maxInt == null) {
            return null;
        }
        //In case the provided integer is in Watts, convert it to kW and cut decimals
        if (maxInt > 1000) {
            return maxInt / 1000;
        }
        return maxInt;
    }
}
