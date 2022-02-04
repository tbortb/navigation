package de.volkswagen.f73.evnavigator.model.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import de.volkswagen.f73.evnavigator.model.Station;

/**
 * OpenCSV helper class for parsing CSV fields
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class StringConverter extends AbstractBeanField<Station, String> {

    @Override
    public String convert(String checkString) {
        return checkString.equals("null") ? null : checkString;
    }
}
