package de.volkswagen.f73.evnavigator.view;

/**
 * TextField that only allows inputting integers
 *
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class IntegerTextField extends RestrictedNumberTextField {

    /**
     * Checks whether an entered string matches the integer pattern
     *
     * @param text input string
     * @return validity
     */
    @Override
    public boolean isAllowedString(String text) {
        return text.matches("[0-9-]+") || text.matches("");
    }
}
