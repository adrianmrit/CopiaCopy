package inputFilters;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import utils.FileTools;

public class FileNameInputFilter extends DocumentFilter {
	private String currentValue;
	private String initialValue;
	private JLabel errorLabel;
	private JButton submitButton;
	
	public FileNameInputFilter(JLabel errorLabel, JButton submitButton, String initialValue) {
		super();
		this.errorLabel = errorLabel;
		this.initialValue = initialValue ;
		this.submitButton = submitButton;
	}
	
	public void insertString(DocumentFilter.FilterBypass fb, int offset,
		String string, AttributeSet attr) throws BadLocationException {
		if (string == null) {
			return;
		} else {
			this.currentValue = checkInput(string, offset);
			fb.insertString(0, this.currentValue, attr);
		}
	}
	
	public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
	throws BadLocationException {
		Document doc = fb.getDocument();
		int currentLength = doc.getLength();
		
		String currentContent = doc.getText(0, currentLength);
		String before = currentContent.substring(0, offset);
		String after = currentContent.substring(length+offset, currentLength);
		String newValue = before + after;
		
		currentValue = checkInput(newValue, offset);
		fb.remove(offset, length);
	}
	
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
			String text, AttributeSet attrs) throws BadLocationException {
			Document doc = fb.getDocument();
			int currentLength = doc.getLength();
			
			String currentContent = doc.getText(0, currentLength);
			String before = currentContent.substring(0, offset);
			String after = currentContent.substring(length+offset, currentLength);
			String newValue = before + (text == null ? "" : text) + after;
			
			currentValue = checkInput(newValue, offset);
			fb.replace(offset, length, text, attrs);
	}
	
	private void handleInvalid(String proposedValue) {
		this.errorLabel.setText(String.format("%s is not a valid name", proposedValue));
		this.errorLabel.setVisible(true);
		this.submitButton.setEnabled(false);
		
		// do not throw, show information to user and disable buttons instead
		// throw new BadLocationException(proposedValue, offset);
	}
	
	private void handleValid(String proposedValue) {
		this.errorLabel.setVisible(false);
		if (proposedValue.equals(initialValue)) {
			this.submitButton.setEnabled(false);
		} else {
			this.submitButton.setEnabled(true);
		}
	}
	
	private String checkInput(String proposedValue, int offset) {
		
		boolean isValid = FileTools.isValidFileName(proposedValue);

		
		if (isValid) {
			handleValid(proposedValue);
		} else {
			handleInvalid(proposedValue);
		}
		
		return proposedValue;
	}
}
