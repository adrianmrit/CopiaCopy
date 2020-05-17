package languages;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

public abstract class ExtendedListResourceBundle extends ListResourceBundle{
	
	/**
	 * Equivalent to String.format(ListResourceBundleObject.getString(key), args)
	 * @param key The resource bundle key
	 * @param args Arguments referenced by the format specifiers in the formatstring
	 * @return A formatted string
	 * @see {@link String#format}
	 */
	public String format(String key, Object... args) {
		return String.format(this.getString(key), args);
	}
	
	/**
	 * Joins multiple strings from the resource bundle according to the order of their keys.
	 * @param separator String to put between all strings joined
	 * @param keys The keys of the string that will be joined
	 * @return
	 */
	public String constructMessage(String separator, String... keys) {
		String result = "";
		for (String key:keys) {
			result += this.getString(key) + separator;
		}
		return result.substring(0, result.length()-separator.length()); // ignore last separator
	}
	
	/**
	 * Joins multiple strings from the resource bundle according to the order of their keys.
	 * A space will be added between the strings joined.
	 * @param keys The keys of the string that will be joined
	 * @return
	 */
	public String constructMessage(String... keys) {
		return this.constructMessage(" ", keys);
	}
	
	/**
	 * Gets an array of objects where each one is the corresponding object
	 * for each key in the resource bundle.
	 * @param keys
	 * @return the object for each key
	 */
	public Object[] getObjects(String... keys) {
		List<Object> objects = new ArrayList<>();
		
		for (String key:keys) {
			objects.add(this.getObject(key));
		}
		
		return objects.toArray();
	}
	
	/**
	 * Returns a formated string, where the string format is a the value for a key,
	 * and the arguments are other values for their corresponding key.
	 * @param key The key that points to the format string.
	 * @param keys The keys that point to the arguments
	 * @return
	 */
	public String internatlFormat(String key, String... keys) {
		Object args = getObjects(keys);
		return String.format(this.getString(key), args);
	}
}
