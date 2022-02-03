package de.volkswagen.f73.evnavigator.application;

import com.sun.javafx.application.PlatformImpl;
import de.volkswagen.f73.evnavigator.controller.*;
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
@TestPropertySource(locations= "classpath:application-test.properties")
class JavaFxTests {

	@Autowired
	FxWeaver fxWeaver;

	private static final List<Class> classes = new ArrayList<>();
	private TextField doubleTestField = new DoubleTextField();
	private TextField integerTestField = new IntegerTextField();
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
	 * Loads the JavaFX environment without actually launching the UI
	 */
	@BeforeAll
	static void setUp() {
		PlatformImpl.startup(() -> {});
		classes.add(MainWindow.class);
		classes.add(Navigation.class);
		classes.add(Menu.class);
		classes.add(SettingsMenu.class);
		classes.add(ManageStations.class);
		classes.add(ManagePlaces.class);
	}

	/**
	 * Loads and initializes each set up class with FxWeaver
	 */
	@BeforeEach
	void init() {
		classes.forEach(clazz -> this.fxWeaver.load(clazz));
	}

	/**
	 * Clear the field before each test, so there is a commun ground to start from for each test
	 */
	@BeforeEach
	private void clearTextField() {
		this.doubleTestField.clear();
		this.integerTestField.clear();
	}


	/**
	 * Asserts that loading a controller initializes its Bean
	 */
	@Test
	void loadingControllersCreatesBeanWithCorrectClassTest() {
		classes.forEach(clazz -> {
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
		Assertions.assertEquals(this.integerOut, this.typeStringToTf(this.integerInp, this.doubleTestField));
	}

	/**
	 * Check if a regular double can be entered into the field
	 */
	@Test
	void doubleInsertDoubleTest() {
		Assertions.assertEquals(this.doubleCaseOut, this.typeStringToTf(this.doubleCaseInp, this.doubleTestField));
	}

	/**
	 * Check that the character d can also not be entered into the field
	 */
	@Test
	void doubleInsertDTest() {
		Assertions.assertEquals(this.containsDOut, this.typeStringToTf(this.containsDInp, this.doubleTestField));
	}

	/**
	 * check that letters can not be entered into the field
	 */
	@Test
	void doubleInsertLettersTest() {
		Assertions.assertEquals(this.lettersOut, this.typeStringToTf(this.lettersInp, this.doubleTestField));
	}

	/**
	 * Check if a point can be entered into the field
	 */
	@Test
	void doubleInsertPointTest() {
		Assertions.assertEquals(this.startPointOut, this.typeStringToTf(this.startPointInp, this.doubleTestField));
	}

	/**
	 * Check if two points can not be entered into the field
	 */
	@Test
	void doubleInsertTwoPointsTest() {
		Assertions.assertEquals(this.twoPointsOut, this.typeStringToTf(this.twoPointsInp, this.doubleTestField));
	}

	/**
	 * Check if a comma can be entered into the field
	 */
	@Test
	void doubleInsertCommaTest() {
		Assertions.assertEquals(this.commaOut, this.typeStringToTf(this.commaInp, this.doubleTestField));
	}

	/**
	 * Check if a combination of comma and point can not be entered into the field
	 * Only the first of these characters should be recognized
	 */
	@Test
	void doubleInsertCommaAndPointTest() {
		Assertions.assertEquals(this.commaAndPointOut, this.typeStringToTf(this.commaAndPointInp, this.doubleTestField));
	}

	/**
	 * Check if a combination of comma and point can not be entered into the field when pasted all together
	 * This test does not simmulate the typeing of a keyboar, but a copy paste action of an unallowed string
	 */
	@Test
	void doublePointAndCommaPasteTest() {
		String unallowedString = this.commaAndPointInp;
		this.doubleTestField.insertText(0, unallowedString);
		Assertions.assertEquals("", this.doubleTestField.getText());
	}

	/**
	 * Test that strings containing letters can not be pasted into the field.
	 * This test does not simmulate the typeing of a keyboar, but a copy paste action of an unallowed string
	 */
	@Test
	void doubleLetterPasteTest() {
		String unallowedString = this.lettersInp;
		this.doubleTestField.insertText(0, unallowedString);
		Assertions.assertEquals("", this.doubleTestField.getText());
	}

	/**
	 * This test does not simmulate the typeing of a keyboar, but a copy paste action of an allowed string
	 */
	@Test
	void doubleAllowedPasteTest() {
		String allowedString = this.doubleCaseInp;
		this.doubleTestField.insertText(0, allowedString);
		Assertions.assertEquals(allowedString, this.doubleTestField.getText());
	}

	/**
	 * Check if a regular integer can be entered into the IntegerTextField
	 */
	@Test
	void integerInsertIntTest() {
		Assertions.assertEquals(this.integerOut, this.typeStringToTf(this.integerInp, this.integerTestField));
	}


	/**
	 * Check if a combination of comma and point can not be entered into the field when pasted all together
	 * This test does not simmulate the typeing of a keyboar, but a copy paste action of an unallowed string
	 */
	@Test
	void integerPointAndCommaPasteTest() {
		String unallowedString = this.commaAndPointInp;
		this.integerTestField.insertText(0, unallowedString);
		Assertions.assertEquals("", this.integerTestField.getText());
	}

	/**
	 * Test that strings containing letters can not be pasted into the field.
	 * This test does not simmulate the typeing of a keyboar, but a copy paste action of an unallowed string
	 */
	@Test
	void integerLetterPasteTest() {
		String unallowedString = this.lettersInp;
		this.integerTestField.insertText(0, unallowedString);
		Assertions.assertEquals("", this.integerTestField.getText());
	}

	/**
	 * Shuts down the JavaFX thread after tests finished.
	 */
	@AfterAll
	static void cleanUp() {
		PlatformImpl.exit();
	}

	/**
	 * This method simulates typing a string via the keyboard, so it inserts the characters one by one
	 */
	private String typeStringToTf(String testString, TextField field) {
		for(int i = 0; i < testString.length(); i++) {
			int insertPos = field.getText().length();
			field.insertText(insertPos, testString.substring(i, i + 1));
		}
		return field.getText();
	}

}
