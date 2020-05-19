package gui;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

import languages.LangBundle;
import themes.Themes;

public class Settings {
	public static final String CONFIG_FILE_NAME = "settings.properties";
	private static PropertiesConfiguration CONFIG;
	private static FileHandler fileHandler;
	
	private static void setDefaultConfig() {
		PropertiesConfiguration config = new PropertiesConfiguration();

		config.addProperty("language", LangBundle.INSTALLED[0]);
		config.addProperty("theme", Themes.INSTALLED[0]);

		CONFIG = config;
	}
	
	public static void createDefaultPropertiesFile() {
		setDefaultConfig();
		save();
	}
	
	private static void loadProperties() {
		Configurations configs = new Configurations();
		try {
			PropertiesConfiguration config = configs.properties(CONFIG_FILE_NAME);
			CONFIG = config;
		} catch (ConfigurationException e) {
			setDefaultConfig();
		}
	}
	
	private static PropertiesConfiguration getConfig() {
		if (CONFIG == null) {
			loadProperties();
		}
		
		return CONFIG;
	}
	
	private static FileHandler getFileHandler() {
		if (fileHandler == null) {
			fileHandler = new FileHandler(getConfig());
		}
		
		return fileHandler;
	}
	
	private static void save() {
		try {
			new FileHandler(getConfig()).save(CONFIG_FILE_NAME);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static void set(String key, Object val) {
		getConfig().setProperty(key, val);
		save();
	}
	
	public static <T> T get(Class<T> type, String key) {
		return getConfig().get(type, key);
	}
	
	public static String getString(String key) {
		return getConfig().getString(key);
	}

}
