package de.volkswagen.f73.evnavigator.application;

import com.sun.javafx.application.PlatformImpl;
import de.volkswagen.f73.evnavigator.controller.*;
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
		classes.add(ManageStation.class);
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
	 * Asserts that loading a controller initializes its Bean
	 */
	@Test
	void loadingControllersCreatesBeanWithCorrectClass() {
		classes.forEach(clazz -> {
			Assertions.assertDoesNotThrow(() -> this.fxWeaver.load(clazz));
			Assertions.assertEquals(clazz, this.fxWeaver.getBean(clazz).getClass());
		});
	}

	/**
	 * Asserts that switching scenes sets the correct Object on the MainWindow
	 */
	@Test
	void settingViewInMainWindowChangesView() {
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
	void backButtonVisibilityIsDependentOnView() {
		Assertions.assertFalse(this.fxWeaver.load(MainWindow.class).getController().getBackButton().isVisible());
		this.fxWeaver.load(Navigation.class).getController().show();
		Assertions.assertTrue(this.fxWeaver.getBean(MainWindow.class).getBackButton().isVisible());
	}

	/**
	 * Shuts down the JavaFX thread after tests finished.
	 */
	@AfterAll
	static void cleanUp() {
		PlatformImpl.exit();
	}

}
