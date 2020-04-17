package inputFilters;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class FileNameInputFilter extends DocumentFilter {
	private String currentValue;
	private String initialValue;
	private static final String[] invalidCharacters = new String[] {"/", "\\"};
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
	
	private String checkInput(String proposedValue, int offset)
		throws BadLocationException {
		System.out.println(proposedValue);
		// check if contains any non valid character
		int invalidChar = 0;
		boolean invalid = false;
		for (int i=0; i<this.invalidCharacters.length; i++) {
			if (proposedValue.contains(this.invalidCharacters[i])) {
//				throw new BadLocationException(proposedValue, offset);
				invalid = true;
				invalidChar = i;
			}
		}
		
		if (invalid) {
			this.errorLabel.setText(this.invalidCharacters[invalidChar] + " is not a valid character.");
			this.submitButton.setEnabled(false);
//			throw new BadLocationException(proposedValue, offset);
		} else {
			this.errorLabel.setText("");
			if (proposedValue.equals(initialValue)) {
				this.submitButton.setEnabled(false);
			} else {
				this.submitButton.setEnabled(true);
			}
		}
		
		return proposedValue;
	}
}
