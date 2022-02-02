package de.volkswagen.f73.evnavigator.model.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import de.volkswagen.f73.evnavigator.model.Station;

import java.util.Arrays;
import java.util.List;

/**
 * OpenCSV helper class for parsing CSV fields
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class FeeConverter extends AbstractBeanField<Station, Boolean> {
    private final List<String> falseKeywords = Arrays.asList("no", "kostenlos", "free", "null");
    private final List<String> trueKeywords = Arrays.asList("yes", "ja", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    @Override
    public Boolean convert(String checkString) {
        //Has fee takes priority over has no fee
        boolean hasFee = this.trueKeywords.stream().anyMatch(s -> checkString.toLowerCase().contains(s));
        if (hasFee) {
            return true;
        }
        boolean hasNoFee = this.falseKeywords.stream().anyMatch(s -> checkString.toLowerCase().contains(s));
        if (hasNoFee) {
            return false;
        }

        //return null (undefined) if no keyword is met
        // TODO error handling
        return null;
    }
}
