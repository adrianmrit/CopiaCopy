package dialogs;

import java.nio.file.Path;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class AbstractErrorDialog implements Dialog{
	/**
	 * Default title for error dialogs.
	 */
	private static final String TITLE = "Copy Error";
	
	public String getTitle() {
		return TITLE;
	}
	
	public String getMessage(Path filePath) {
		return String.format(getFormatString(), filePath.toString());
	}
	
	public int showDialog(JFrame frame, Path path) {
//		JOptionPane optionPane = new JOptionPane(getMessage(path), JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);

//		JDialog dialog = optionPane.createDialog(frame, "Copy error");
//		dialog.setVisible(true);
//		return (int) optionPane.getValue();
		
		return JOptionPane.showConfirmDialog(frame, getMessage(path), getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
	}

}
