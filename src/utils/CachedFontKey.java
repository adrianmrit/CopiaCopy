package utils;

public class CachedFontKey {
	private String path;
	private float size;
	
	public CachedFontKey(String path, float size) {
		if (path == null) {
            throw new IllegalArgumentException("Argument null");
        }
		this.path = path;
		this.size = size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + Float.floatToIntBits(size);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CachedFontKey other = (CachedFontKey) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (Float.floatToIntBits(size) != Float.floatToIntBits(other.size))
			return false;
		return true;
	}
}
