package de.volkswagen.f73.evnavigator;

import com.sun.javafx.application.PlatformImpl;
import de.saxsys.javafx.test.JfxRunner;
import de.volkswagen.f73.evnavigator.application.JavaFxApplication;
import de.volkswagen.f73.evnavigator.controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import net.rgielen.fxweaver.core.FxWeaver;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
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
		fxWeaver.load(Navigation.class).getController().show();
		Assertions.assertEquals("Navigation", fxWeaver.getBean(MainWindow.class).getTitle().getText());

		fxWeaver.load(Menu.class).getController().show();
		Assertions.assertEquals("Main Menu", fxWeaver.getBean(MainWindow.class).getTitle().getText());

		fxWeaver.load(SettingsMenu.class).getController().show();
		Assertions.assertEquals("Settings", fxWeaver.getBean(MainWindow.class).getTitle().getText());
	}

	@AfterAll
	static void cleanUp() {
		// shut down FX thread after tests
		PlatformImpl.exit();
	}

}
