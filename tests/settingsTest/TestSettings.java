package settingsTest;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.junit.jupiter.api.Test;

import gui.Settings;
import gui.SettingsKeys;

class TestSettings {

	@Test
	void testDefaults() {
		Settings.createDefaultPropertiesFile();
		
		assertTrue(Files.exists(Paths.get(Settings.CONFIG_FILE_NAME), LinkOption.NOFOLLOW_LINKS));
	}
	
	@Test
	void testGetSettings() {
//		Settings.createDefaultPropertiesFile();
//		
//		PropertiesConfiguration config = Settings.getProperties();
		assertEquals("spanish", Settings.getString(SettingsKeys.LANGUAGE));
	}

}
