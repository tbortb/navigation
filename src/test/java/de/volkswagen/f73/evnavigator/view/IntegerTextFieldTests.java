package de.volkswagen.f73.evnavigator.view;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
public class IntegerTextFieldTests {
    private TextField testField = new IntegerTextField();
    private String integerInp = "25";
    private String integerOut = "25";
    private String lettersInp = "2A5";
    private String commaAndPointInp = "25,25.38";

    /**
     * Setup the test, so that also a Javafx component can be tested
     */
    @BeforeAll
    public static void setUp() {
        JFXPanel panel = new JFXPanel();
    }

    /**
     * Clear the field before each test, so there is a commun ground to start from for each test
     */
    @BeforeEach
    private void clearTextField() {
        this.testField.clear();
    }

    /**
     * Check if a regular integer can be entered into the DoubleTextField
     */
    @Test
    void testInsertInt() {
        Assertions.assertEquals(this.integerOut, this.typeStringToTf(this.integerInp));
    }


    /**
     * Check if a combination of comma and point can not be entered into the field when pasted all together
     * This test does not simmulate the typeing of a keyboar, but a copy paste action of an unallowed string
     */
    @Test
    void testPointAndCommaPaste() {
        String unallowedString = this.commaAndPointInp;
        this.testField.insertText(0, unallowedString);
        Assertions.assertEquals("", this.testField.getText());
    }

    /**
     * Test that strings containing letters can not be pasted into the field.
     * This test does not simmulate the typeing of a keyboar, but a copy paste action of an unallowed string
     */
    @Test
    void testLetterPaste() {
        String unallowedString = this.lettersInp;
        this.testField.insertText(0, unallowedString);
        Assertions.assertEquals("", this.testField.getText());
    }

    /**
     * This method simulates typing a string via the keyboard, so it inserts the characters one by one
     */
    private String typeStringToTf(String testString) {
        for(int i = 0; i < testString.length(); i++) {
            int insertPos = this.testField.getText().length();
            this.testField.insertText(insertPos, testString.substring(i, i + 1));
        }
        return this.testField.getText();
    }
}
