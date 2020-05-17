package copy;

import java.util.HashMap;
import java.util.Map;

public class InfoChunkMap extends HashMap<String, InfoChunk>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7824721424658643367L;
//	private Map<String, InfoChunk> chunks;
	
	public InfoChunkMap() {
		super();
	}
	
	public void put(InfoChunk chunk) {
		super.put(chunk.getKey(), chunk);
	}
	
	public <T> void createChunk(String key, Object value) {
		super.put(key, new InfoChunk(key, value));
	}
	
	public <T> T getChunkValue(String key) {
		return this.get(key).getValue();
	}

}
