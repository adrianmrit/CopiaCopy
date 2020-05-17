package dialogs;

import java.nio.file.Path;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import copy.CopiableFile;
import enums.ConflictAction;
import languages.LangBundle;
import models.SuperModel;

public class WritePermissionErrorDialog extends AbstractErrorDialog{
	
	public String getFormatString() {
		return LangBundle.CURRENT.getString("WritePermissionError");
	};
}
