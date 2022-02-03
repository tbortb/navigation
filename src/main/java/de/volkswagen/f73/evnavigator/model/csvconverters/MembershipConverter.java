package de.volkswagen.f73.evnavigator.model.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import de.volkswagen.f73.evnavigator.model.Station;

/**
 * OpenCSV helper class for parsing CSV fields
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class MembershipConverter extends AbstractBeanField<Station, Boolean> {

    @Override
    public Boolean convert(String s) {
        //Special cases: If String contains yes and no, it counts as no. If it contains neither it is null (undefined)
        if (s.toLowerCase().contains("no")) {
            return false;
        } else if (s.toLowerCase().contains("yes")) {
            return true;
        }
        return null;
    }
}
