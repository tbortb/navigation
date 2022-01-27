package de.volkswagen.f73.evnavigator;

import com.sun.javafx.application.PlatformImpl;
import de.volkswagen.f73.evnavigator.controller.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import net.rgielen.fxweaver.core.FxWeaver;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@TestPropertySource(locations= "classpath:application-test.properties")
class JavaFxTests {

	@Autowired
	FxWeaver fxWeaver;

	private static List<Class> classes = new ArrayList<>();

	@BeforeAll
	static void setUp() {
		// make the JavaFX environment load
		PlatformImpl.startup(() -> {});
		classes.add(MainWindow.class);
		classes.add(Navigation.class);
		classes.add(Menu.class);
		classes.add(SettingsMenu.class);
		classes.add(AddStation.class);
		classes.add(AddPoi.class);
	}

	@BeforeEach
	void init() {
		classes.forEach(clazz -> fxWeaver.load(clazz));
	}


	@Test
	void loadingControllersAndViews() {
		classes.forEach(clazz -> {
			Assertions.assertDoesNotThrow(() -> fxWeaver.load(clazz));
			Assertions.assertNotNull(fxWeaver.load(clazz));
		});
	}

	@Test
	void loadingBeans() {
		classes.forEach(clazz -> {
			Assertions.assertDoesNotThrow(() -> fxWeaver.getBean(clazz));
			Assertions.assertNotNull(fxWeaver.getBean(clazz));
		});
	}

	@Test
	void setViewInMainWindow() {
		fxWeaver.load(SettingsMenu.class).getController().show();
		Assertions.assertEquals("menuBox", fxWeaver.getBean(MainWindow.class).getRootPane().getCenter().getId());
		Assertions.assertEquals("Settings", fxWeaver.getBean(MainWindow.class).getTitle().getText());

		fxWeaver.load(Navigation.class).getController().show();
		Assertions.assertEquals("navigationPane", fxWeaver.getBean(MainWindow.class).getRootPane().getCenter().getId());
		Assertions.assertEquals("Navigation", fxWeaver.getBean(MainWindow.class).getTitle().getText());

		fxWeaver.load(Menu.class).getController().show();
		Assertions.assertEquals("menuBox", fxWeaver.getBean(MainWindow.class).getRootPane().getCenter().getId());
		Assertions.assertEquals("Main Menu", fxWeaver.getBean(MainWindow.class).getTitle().getText());
	}

	@AfterAll
	static void cleanUp() {
		// shut down FX thread after tests
		PlatformImpl.exit();
	}

}
