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
class DoubleTextFieldTest {
    private TextField testField = new DoubleTextField();
    private String integerInp = "25";
    private String integerOut = "25";
    private String doubleCaseInp = "25.378";
    private String doubleCaseOut = "25.378";
    private String containsDInp = "25.3d";
    private String containsDOut = "25.3";
    private String lettersInp = "2A5.2ö5d";
    private String lettersOut = "25.25";
    private String commaAndPointInp = "25,25.38";
    private String commaAndPointOut = "25.2538";
    private String startPointInp = ".38";
    private String startPointOut = "38";
    private String twoPointsInp = "38.25.2";
    private String twoPointsOut = "38.252";
    private String commaInp = "25,25";
    private String commaOut = "25.25";

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
     * Check if a regular double can be entered into the field
     */
    @Test
    void testInsertDouble() {
        Assertions.assertEquals(this.doubleCaseOut, this.typeStringToTf(this.doubleCaseInp));
    }

    /**
     * Check that the character d can also not be entered into the field
     */
    @Test
    void testInsertD() {
        Assertions.assertEquals(this.containsDOut, this.typeStringToTf(this.containsDInp));
    }

    /**
     * check that letters can not be entered into the field
     */
    @Test
    void testInsertLetters() {
        Assertions.assertEquals(this.lettersOut, this.typeStringToTf(this.lettersInp));
    }

    /**
     * Check if a point can be entered into the field
     */
    @Test
    void testInsertPoint() {
        Assertions.assertEquals(this.startPointOut, this.typeStringToTf(this.startPointInp));
    }

    /**
     * Check if two points can not be entered into the field
     */
    @Test
    void testInsertTwoPoints() {
        Assertions.assertEquals(this.twoPointsOut, this.typeStringToTf(this.twoPointsInp));
    }

    /**
     * Check if a comma can be entered into the field
     */
    @Test
    void testInsertComma() {
        Assertions.assertEquals(this.commaOut, this.typeStringToTf(this.commaInp));
    }

    /**
     * Check if a combination of comma and point can not be entered into the field
     * Only the first of these characters should be recognized
     */
    @Test
    void testInsertCommaAndPoint() {
        Assertions.assertEquals(this.commaAndPointOut, this.typeStringToTf(this.commaAndPointInp));
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
     * This test does not simmulate the typeing of a keyboar, but a copy paste action of an allowed string
     */
    @Test
    void testAllowedPaste() {
        String allowedString = this.doubleCaseInp;
        this.testField.insertText(0, allowedString);
        Assertions.assertEquals(allowedString, this.testField.getText());
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
