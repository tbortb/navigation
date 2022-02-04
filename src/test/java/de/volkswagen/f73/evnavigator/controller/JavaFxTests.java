package de.volkswagen.f73.evnavigator.controller;

import com.sun.javafx.application.PlatformImpl;
import de.volkswagen.f73.evnavigator.model.Place;
import de.volkswagen.f73.evnavigator.model.Station;
import de.volkswagen.f73.evnavigator.view.DoubleTextField;
import de.volkswagen.f73.evnavigator.view.IntegerTextField;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxWeaver;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class JavaFxTests {

    private static final List<Class> CLASSES = new ArrayList<>();
    private static final String INTEGER_IN = "25";
    private static final String INTEGER_OUT = "25";
    private static final String DOUBLE_CASE_IN = "25.378";
    private static final String DOUBLE_CASE_OUT = "25.378";
    private static final String CONTAINS_D_IN = "25.3d";
    private static final String CONTAINS_D_OUT = "25.3";
    private static final String LETTERS_IN = "2A5.2ö5d";
    private static final String LETTERS_OUT = "25.25";
    private static final String COMMA_POINT_IN = "25,25.38";
    private static final String COMMA_POINT_OUT = "25.2538";
    private static final String STARTPOINT_IN = ".38";
    private static final String STARTPOINT_OUT = "38";
    private static final String TWO_POINTS_IN = "38.25.2";
    private static final String TWO_POINTS_OUT = "38.252";
    private static final String COMMA_IN = "25,25";
    private static final String COMMA_OUT = "25.25";
    private final TextField doubleTextField = new DoubleTextField();
    private final TextField integerTextField = new IntegerTextField();
    @Autowired
    FxWeaver fxWeaver;

    /**
     * Loads the JavaFX environment without actually launching the UI
     */
    @BeforeAll
    static void setUp() {
        PlatformImpl.startup(() -> {
        });
        CLASSES.add(MainWindow.class);
        CLASSES.add(Navigation.class);
        CLASSES.add(Menu.class);
        CLASSES.add(SettingsMenu.class);
        CLASSES.add(ManageStations.class);
        CLASSES.add(ManagePlaces.class);
    }

    /**
     * Shuts down the JavaFX thread after tests finished.
     */
    @AfterAll
    static void cleanUp() {
        PlatformImpl.exit();
    }

    /**
     * Loads and initializes each set up class with FxWeaver
     */
    @BeforeEach
    void init() {
        CLASSES.forEach(clazz -> this.fxWeaver.load(clazz));
        this.doubleTextField.clear();
        this.integerTextField.clear();
    }

    /**
     * Asserts that loading a controller initializes its Bean
     */
    @Test
    void loadingControllersCreatesBeanWithCorrectClassTest() {
        CLASSES.forEach(clazz -> {
            Assertions.assertDoesNotThrow(() -> this.fxWeaver.load(clazz));
            Assertions.assertEquals(clazz, this.fxWeaver.getBean(clazz).getClass());
        });
    }

    /**
     * Asserts that switching scenes sets the correct Object on the MainWindow
     */
    @Test
    void settingViewInMainWindowChangesViewTest() {
        this.fxWeaver.load(SettingsMenu.class).getController().show();
        Assertions.assertEquals("menuBox", this.fxWeaver.getBean(MainWindow.class).getRootPane().getCenter().getId());
        Assertions.assertEquals("Settings", this.fxWeaver.getBean(MainWindow.class).getTitle().getText());

        this.fxWeaver.load(Navigation.class).getController().show();
        Assertions.assertEquals("navigationPane", this.fxWeaver.getBean(MainWindow.class).getRootPane().getCenter().getId());
        Assertions.assertEquals("Navigation", this.fxWeaver.getBean(MainWindow.class).getTitle().getText());

        this.fxWeaver.load(Menu.class).getController().show();
        Assertions.assertEquals("menuBox", this.fxWeaver.getBean(MainWindow.class).getRootPane().getCenter().getId());
        Assertions.assertEquals("Main Menu", this.fxWeaver.getBean(MainWindow.class).getTitle().getText());
    }

    /**
     * Asserts that the navigation button reacts to changes of the view
     */
    @Test
    void backButtonVisibilityIsDependentOnViewTest() {
        Assertions.assertFalse(this.fxWeaver.load(MainWindow.class).getController().getBackButton().isVisible());
        this.fxWeaver.load(Navigation.class).getController().show();
        Assertions.assertTrue(this.fxWeaver.getBean(MainWindow.class).getBackButton().isVisible());
    }

    /**
     * Check if a regular integer can be entered into the DoubleTextField
     */
    @Test
    void doubleInsertIntTest() {
        Assertions.assertEquals(INTEGER_OUT, this.typeStringToTf(INTEGER_IN, this.doubleTextField));
    }

    /**
     * Check if a regular double can be entered into the DoubleTextField
     */
    @Test
    void doubleInsertDoubleTest() {
        Assertions.assertEquals(DOUBLE_CASE_OUT, this.typeStringToTf(DOUBLE_CASE_IN, this.doubleTextField));
    }

    /**
     * Check that the character d can also not be entered into the DoubleTextField
     */
    @Test
    void doubleInsertDTest() {
        Assertions.assertEquals(CONTAINS_D_OUT, this.typeStringToTf(CONTAINS_D_IN, this.doubleTextField));
    }

    /**
     * Check that letters can not be entered into the DoubleTextField
     */
    @Test
    void doubleInsertLettersTest() {
        Assertions.assertEquals(LETTERS_OUT, this.typeStringToTf(LETTERS_IN, this.doubleTextField));
    }

    /**
     * Check if a point can be entered into the DoubleTextField
     */
    @Test
    void doubleInsertPointTest() {
        Assertions.assertEquals(STARTPOINT_OUT, this.typeStringToTf(STARTPOINT_IN, this.doubleTextField));
    }

    /**
     * Check if two points can not be entered into the DoubleTextField
     */
    @Test
    void doubleInsertTwoPointsTest() {
        Assertions.assertEquals(TWO_POINTS_OUT, this.typeStringToTf(TWO_POINTS_IN, this.doubleTextField));
    }

    /**
     * Check if a comma can be entered into the DoubleTextField
     */
    @Test
    void doubleInsertCommaTest() {
        Assertions.assertEquals(COMMA_OUT, this.typeStringToTf(COMMA_IN, this.doubleTextField));
    }

    /**
     * Check if a combination of comma and point can not be entered into the DoubleTextField
     * Only the first of these characters should be recognized
     */
    @Test
    void doubleInsertCommaAndPointTest() {
        Assertions.assertEquals(COMMA_POINT_OUT, this.typeStringToTf(COMMA_POINT_IN, this.doubleTextField));
    }

    /**
     * Check if a combination of comma and point can not be entered into the field when pasted all together.
     * This test does not simmulate the typeing of a keyboar, but a copy paste action of an unallowed string
     */
    @Test
    void doublePointAndCommaPasteTest() {
        this.doubleTextField.insertText(0, COMMA_POINT_IN);
        Assertions.assertEquals("", this.doubleTextField.getText());
    }

    /**
     * Test that strings containing letters can not be pasted into the DoubleTextField.
     * This test does not simmulate the typeing of a keyboar, but a copy paste action of an unallowed string
     */
    @Test
    void doubleLetterPasteTest() {
        this.doubleTextField.insertText(0, LETTERS_IN);
        Assertions.assertEquals("", this.doubleTextField.getText());
    }

    /**
     * This test does not simulate the typing of a keyboard, but a copy and paste action of an allowed string
     */
    @Test
    void doubleAllowedPasteTest() {
        String allowedString = DOUBLE_CASE_IN;
        this.doubleTextField.insertText(0, allowedString);
        Assertions.assertEquals(allowedString, this.doubleTextField.getText());
    }

    /**
     * Check if a regular integer can be entered into the IntegerTextField
     */
    @Test
    void integerInsertIntTest() {
        Assertions.assertEquals(INTEGER_OUT, this.typeStringToTf(INTEGER_IN, this.integerTextField));
    }

    /**
     * Check if a combination of comma and point can not be entered into the IntegerTextField when pasted all together
     * This test does not simmulate the typeing of a keyboar, but a copy paste action of an unallowed string
     */
    @Test
    void integerPointAndCommaPasteTest() {
        this.integerTextField.insertText(0, COMMA_POINT_IN);
        Assertions.assertEquals("", this.integerTextField.getText());
    }

    /**
     * Test that strings containing letters can not be pasted into the IntegerTextField.
     * This test does not simmulate the typeing of a keyboar, but a copy paste action of an unallowed string
     */
    @Test
    void integerLetterPasteTest() {
        this.integerTextField.insertText(0, LETTERS_IN);
        Assertions.assertEquals("", this.integerTextField.getText());
    }

    /**
     * Tests whether the ManageStations controller is able to create a new Station object from the input fields
     */
    @Test
    void manageStationsControllerNewStationTest() {
        this.fxWeaver.load(ManageStations.class);
        this.fxWeaver.getBean(ManageStations.class).nameInput.setText("TestStation");
        Station testStation = Assertions
                .assertDoesNotThrow(() -> this.fxWeaver.getBean(ManageStations.class).createNewPlace());
        Assertions.assertEquals("TestStation", testStation.getName());
    }

    /**
     * Tests whether the ManageStations controller is able to update an existing Station object from the input fields
     */
    @Test
    void manageStationsControllerUpdateStationTest() {
        this.fxWeaver.load(ManageStations.class);
        this.fxWeaver.getBean(ManageStations.class).selectedPlace = new Station.Builder().withName("test").build();
        this.fxWeaver.getBean(ManageStations.class).nameInput.setText("updated");

        Station testStation = Assertions
                .assertDoesNotThrow(() -> this.fxWeaver.getBean(ManageStations.class).updatePlace());
        Assertions.assertEquals("updated", testStation.getName());
    }

    /**
     * Tests whether the ManagePlaces controller is able to create a new Place object from the input fields
     */
    @Test
    void managePlacesControllerNewStationTest() {
        this.fxWeaver.load(ManagePlaces.class);
        this.fxWeaver.getBean(ManagePlaces.class).nameInput.setText("TestPlace");
        this.fxWeaver.getBean(ManagePlaces.class).latitudeInput.setText("11.0");
        this.fxWeaver.getBean(ManagePlaces.class).longitudeInput.setText("6.0");
        Place testPlace = Assertions
                .assertDoesNotThrow(() -> this.fxWeaver.getBean(ManagePlaces.class).createNewPlace());
        Assertions.assertEquals("TestPlace", testPlace.getName());
        Assertions.assertEquals(11.0, testPlace.getLat());
        Assertions.assertEquals(6.0, testPlace.getLon());
    }

    /**
     * Tests whether the ManagePlaces controller is able to update an existing Place object from the input fields
     */
    @Test
    void managePlacesControllerUpdateStationTest() {
        this.fxWeaver.load(ManagePlaces.class);
        this.fxWeaver.getBean(ManagePlaces.class).selectedPlace = new Place("test", 10.0, 5.0);
        this.fxWeaver.getBean(ManagePlaces.class).nameInput.setText("updated");
        this.fxWeaver.getBean(ManagePlaces.class).latitudeInput.setText("11.0");
        this.fxWeaver.getBean(ManagePlaces.class).longitudeInput.setText("6.0");

        Place testPlace = Assertions
                .assertDoesNotThrow(() -> this.fxWeaver.getBean(ManagePlaces.class).updatePlace());
        Assertions.assertEquals("updated", testPlace.getName());
        Assertions.assertEquals(11.0, testPlace.getLat());
        Assertions.assertEquals(6.0, testPlace.getLon());
    }

    /**
     * This method simulates typing a string via the keyboard, so it inserts the characters one by one
     */
    private String typeStringToTf(String testString, TextField field) {
        for (int i = 0; i < testString.length(); i++) {
            int insertPos = field.getText().length();
            field.insertText(insertPos, testString.substring(i, i + 1));
        }
        return field.getText();
    }

}
