package de.volkswagen.f73.evnavigator.util.csv_converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import de.volkswagen.f73.evnavigator.model.Station;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class FeeConverter  extends AbstractBeanField<Station, Boolean> {
    private List<String> falseKeywords = Arrays.asList("no", "kostenlos", "free");
    private List<String> trueKeywords = Arrays.asList("yes", "ja", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    @Override
    public Boolean convert(String checkString) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        //Has fee takes priority over has no fee
        boolean hasFee = trueKeywords.stream().anyMatch(s -> checkString.toLowerCase().contains(s));
        if (hasFee){
            return true;
        }
        boolean hasNoFee = falseKeywords.stream().anyMatch(s -> checkString.toLowerCase().contains(s));
        if (hasNoFee){
            return false;
        }

        //return null (undefined) if no keyword is met
        return null;
    }
}
