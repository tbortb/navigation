package de.volkswagen.f73.evnavigator.util.csv_converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import de.volkswagen.f73.evnavigator.model.Station;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class MembershipConverter extends AbstractBeanField<Station, Boolean> {

    @Override
    public Boolean convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        //Special cases: If String contains yes and no, it counts as no. If it contains neither is is null (undefined)
        if (s.toLowerCase().contains("no")){
            return false;
        }else if(s.toLowerCase().contains("yes")){
            return true;
        }
        return null;
    }
}
