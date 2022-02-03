package de.volkswagen.f73.evnavigator.view;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class IntegerTextField extends RestrictedNumberTextField{

    @Override
    public boolean isAllowedString(String text) {
        return text.matches("[0-9-]+") || text.matches("");
    }
}
