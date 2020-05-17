package dialogs;

import java.nio.file.Path;

import javax.swing.JFrame;

public interface Dialog {
	/**
	 * Gets a string format at specified in {@link java.util.Formatter}, 
	 * that will be used to show the message.
	 * @return
	 */
	String getFormatString();
	
	/**
	 * Gets the message that will be shown in the dialog.
	 * @param filePath the path to include in the dialog
	 * @return message to show
	 */
	String getMessage(Path filePath);
	
	/**
	 * Gets the title of the dialog.
	 * @return title
	 */
	String getTitle();
	
	/**
	 * Shows the dialog and gets the response.
	 * @param frame parent frame
	 * @param path path to include in the dialog
	 * @return selected value
	 */
	int showDialog(JFrame frame, Path path);
}
