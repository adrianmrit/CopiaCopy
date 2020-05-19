package themes;

import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;
import mdlaf.themes.MaterialTheme;

public class Themes {
	public static String[] INSTALLED = {LiteCustom.NAME, JMarsDarkCustom.NAME, OceanicCustom.NAME};
	
	public static void setTheme(MaterialTheme theme) {
		try {
			JDialog.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel(new MaterialLookAndFeel (theme));
		}
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace ();
		}
	}

}
