package themes;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

import javafx.scene.text.Font;
import mdlaf.themes.MaterialLiteTheme;
import mdlaf.utils.MaterialBorders;
import mdlaf.utils.MaterialColors;
import mdlaf.utils.MaterialFontFactory;
import utils.FontFactory;

public class LiteCustom extends MaterialLiteTheme{
	@Override
    protected void installColor() {
			super.installColor();
		 this.backgroundProgressBar = MaterialColors.GREEN_50;
	     this.foregroundProgressBar = MaterialColors.GREEN_400;
	     UIManager.put("ProgressBar.selectionForeground", MaterialColors.BLACK);
	     UIManager.put("ProgressBar.selectionBackground", MaterialColors.BLACK);
	}
	
	@Override
    protected void installFonts(){
        this.fontBold = FontFactory.getCachedFont("resources/fonts/Roboto/Roboto-Bold.ttf", 9);
        this.fontItalic = FontFactory.getCachedFont("resources/fonts/Roboto/Roboto-ThinItalic.ttf", 9);
        this.fontMedium = FontFactory.getCachedFont("resources/fonts/Roboto/Roboto-Medium.ttf", 9);
        this.fontRegular = FontFactory.getCachedFont("resources/fonts/Roboto/Roboto-Regular.ttf", 9);

        super.borderTitledBorder = MaterialBorders.LIGHT_LINE_BORDER;
    }
}
