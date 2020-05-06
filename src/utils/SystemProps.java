package utils;

public class SystemProps {
	public static final String BOOLEAN_TRUE = "t";
	public static final String BOOLEAN_FALSE = "f";
	
	/**
	 * Sets a boolean system property
	 * @param key
	 * @param val the boolean property
	 */
	public static void setBooleanProp(String key, boolean val) {
		System.setProperty(key.toUpperCase(), val ? BOOLEAN_TRUE : BOOLEAN_FALSE);
	}
	
	/**
	 * Gets the boolean property for some key. If key is null then it defaults to false
	 * @param key 
	 * @return boolean property
	 */
	public static boolean getBooleanProp(String key) {
		if (System.getProperty(key.toUpperCase(), BOOLEAN_FALSE).equals(BOOLEAN_TRUE)) {
			return true;
		}
		return false;
	}
}
