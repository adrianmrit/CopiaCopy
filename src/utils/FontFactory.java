package utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.FontUIResource;

import mdlaf.utils.MaterialFontFactory;

public class FontFactory {
	private static Map<CachedFontKey, Font> cacheFont = new HashMap<>();
	
	/**
	 * Loads a cached {@link FontUIResource}. If the font is not cached, loads it again.
	 * @param fontPath
	 * @param size
	 * @return
	 */
	public static FontUIResource getCachedFont(String fontPath, float size) {
        if (fontPath == null) {
            throw new IllegalArgumentException("Argument null");
        }
        
        CachedFontKey fontKey = new CachedFontKey(fontPath, size);
        
        if (cacheFont.containsKey(fontKey)) {
            return new FontUIResource(cacheFont.get(fontKey));
        }
        Font font = loadFont(fontPath, size);
        cacheFont.put(fontKey, font);
        return new FontUIResource(cacheFont.get(fontKey));
    }
	
	/**
	 * Loads a Font from a path, with a size
	 * @param path
	 * @param size
	 * @return the loaded font
	 */
	public static Font loadFont(String path, float size) {
		int resolution = Math.min(Toolkit.getDefaultToolkit().getScreenResolution(), 96); // as in MaterialFontFactory
        size = size * resolution / 72.0f;
		try {
			return Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return MaterialFontFactory.getInstance().getFont(MaterialFontFactory.REGULAR);
		}
	}

}
