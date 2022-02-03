package de.volkswagen.f73.evnavigator.view;

import javafx.scene.control.TextField;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class DoubleTextField extends RestrictedNumberTextField {

    @Override
    public boolean isAllowedString(String text) {
        return text.matches("[0-9.,-]+") || text.matches("");
    }

}
