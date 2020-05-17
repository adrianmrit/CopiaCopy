package copy;

import java.util.HashMap;
import java.util.Map;

/**
 * An object that can hold any type of data
 * @author adrianmrit
 */
public class InfoChunk {
	private String key;
	private Object value;
	private Class<?> type;
	
	/**
	 * Creates a chunk of data with a key and a value.
	 * @param <T>
	 * @param key Identifier for this chunk
	 * @param value the value
	 * @param valueType the class type for the value
	 * @throws IllegalArgumentException if <code>value<code> and <code>valueType<code> are not of the same type
	 */
	public <T> InfoChunk(String key, Object value) throws IllegalArgumentException{
//		if (value.getClass() != valueType) {
//			throw new IllegalArgumentException("value and type must be of the same class");
//		}
		this.key = key;
		this.value = value;
		this.type = value.getClass();
	}
	
	public String getKey() {
        return key;
    }
	
	public Class<?> getType(){
		return type;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue() {
        return (T) type.cast(value);
    }
}
