package languages;

import java.util.Locale;

import javax.swing.UIManager;

import mdlaf.utils.MaterialColors;

public class LangBundle {
	public static final Locale ES_LOCALE = Locale.forLanguageTag("es");
	public static final Locale EN_LOCALE = Locale.ENGLISH;
	public static ExtendedListResourceBundle CURRENT;
	public static String[] INSTALLED = new String[] {"spanish", "english"};
	
	public static void load(Locale lang) {
		if (lang.equals(ES_LOCALE)) {
			Locale.setDefault(ES_LOCALE);
			CURRENT = new ESBundle();
		} else {
			Locale.setDefault(EN_LOCALE);
			Locale.setDefault(EN_LOCALE);
			CURRENT = new ESBundle(); // TODO: create english bundle
		}
	}
}
