package de.volkswagen.f73.evnavigator.view;

/**
 * TextField that only allows inputting Doubles
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class DoubleTextField extends RestrictedNumberTextField {

    /**
     * Checks whether an entered string matches the double pattern
     *
     * @param text input string
     * @return validity
     */
    @Override
    public boolean isAllowedString(String text) {
        return text.matches("[0-9.,-]+") || text.matches("");
    }
}
