package de.volkswagen.f73.evnavigator.view;

import javafx.scene.control.TextField;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class DoubleTextField extends TextField {

    @Override
    public void replaceText(int start, int end, String text) {
        if (text.matches("[0-9.,]+") || text.matches("")) {
            text = text.replace(',', '.');
            try {
                Double.parseDouble(this.getText() + text);
                super.replaceText(start, end, text);
            } catch (IllegalArgumentException e) {
                // Do nothing, the purose is to not call replaceText
            }
        }
    }
}
