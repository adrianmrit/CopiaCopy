package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

import components.BigButton;
import components.UnstyledButton;
import copy.SizeRep;
import icons.ArrowIconBottom;
import icons.ArrowIconRight;
import inputFilters.FileNameInputFilter;

// TODO: CLEAN

public class FileExistsDialog extends AbstractDialog{
	private Document renamedTextDocument;
	private JDialog frame;
	private File orig;
	private File dest;
	
	public static final String CANCEL = "Cancel";
	public static final String SKIP = "Skip";
	public static final String RENAME = "Rename";
	public static final String REPLACE = "Replace";
	
	public FileExistsDialog(JFrame parent, File orig, File dest){
		super(parent);
		this.orig = orig;
		this.dest = dest;
	}
	
	public Box getFileBox(File f, String label) {
		double fileSize = SizeRep.readableVal(f.length());
		String fileSizeRep = SizeRep.readableRep(f.length());
		Date lastMod = new Date(f.lastModified());
		JLabel origLabel = new JLabel(label);
		JLabel origSizeLabel = new JLabel(String.format("Size: %.2f%s", fileSize, fileSizeRep));
		JLabel lastModLabel = new JLabel("Last modified:");
		JLabel origModLabel = new JLabel(String.format("%1$tF %1$tr", lastMod));
		
		// Origin file container
		Box fielBox = Box.createVerticalBox();
		fielBox.add(origLabel, BorderLayout.CENTER);
		fielBox.add(origSizeLabel, BorderLayout.CENTER);
		fielBox.add(lastModLabel, BorderLayout.CENTER);
		fielBox.add(origModLabel, BorderLayout.CENTER);
		
		return fielBox;
	}
	
	public void run() {
		this.getParent().setEnabled(false);
		this.frame = new JDialog(this.getParent(), "Copy Sample", true);
		this.frame.setLayout(new GridBagLayout());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		/*************************************
		 * Message Section
		 ************************************/
		
		JLabel messageLabel = new JLabel("Replace file \"" + this.dest.getName() + "\"?");
		JLabel informationLabel = new JLabel(
				"<HTML><b>Anoter file with the same name already exists. " + ""
				+ "Replacing will overwrite its content</b></HTML>"
		);
		Box messagesBox = Box.createVerticalBox();
		messagesBox.add(messageLabel, BorderLayout.CENTER);
		messagesBox.add(informationLabel, BorderLayout.CENTER);
		/*************************************
		 * End Message Section
		 ************************************/
		
		/*************************************
		 * Files Section
		 ************************************/
		
		Box replaceBox = getFileBox(this.dest, "Replace with");
		Box origBox = getFileBox(this.orig, "Original file");
		
		// Files boxes container
		Box filesBox = Box.createHorizontalBox();
		filesBox.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		filesBox.add(origBox, BorderLayout.CENTER);
		filesBox.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		filesBox.add(replaceBox, BorderLayout.CENTER);
		filesBox.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		
		/*************************************
		 * End Files Section
		 ************************************/
		
		/*************************************
		 * Rename Section
		 ************************************/
		UnstyledButton renameToggleButton = new UnstyledButton("<HTML><b>Rename</b></HTML>");
		
		int icoHeight = 8;
		int icoWidth = 5;
		Icon icoUnnactive = new ArrowIconRight(new Color(0, 0, 0), icoWidth, icoHeight);
		renameToggleButton.setIcon(icoUnnactive);
		
		Icon icoActive = new ArrowIconBottom(new Color(0, 0, 0), icoHeight, icoWidth);  // exchange sizes
		
		JTextField renameField = new JTextField(this.orig.getName());
		BigButton renameButton = new BigButton("Rename");
		renameButton.setEnabled(false);
		JLabel renameErrorLabel = new JLabel("");
		renameErrorLabel.setVisible(false);
		
		renameButton.setMargin(new Insets(0,0,0,0));
		renameField.setMargin(new Insets(0,0,0,0));
		Box renameBox = Box.createHorizontalBox();
		renameBox.add(renameField, BorderLayout.CENTER);
		renameBox.add(renameButton, BorderLayout.CENTER);
		renameBox.setVisible(false);
		
		ActionListener toggleRenameListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if(renameBox.isVisible()) {
					renameBox.setVisible(false);
					renameToggleButton.setIcon(icoUnnactive);
				} else {
					renameBox.setVisible(true);
					renameToggleButton.setIcon(icoActive);
				}
			}
		};
		
		renameToggleButton.addActionListener(toggleRenameListener);
		
		this.registerButton(renameButton, RENAME);

		this.renamedTextDocument = renameField.getDocument();
		DocumentFilter fileNameFilter = new FileNameInputFilter(renameErrorLabel, renameButton, this.orig.getName());
		((AbstractDocument)renamedTextDocument).setDocumentFilter(fileNameFilter);
		/*************************************
		 * End Rename Section
		 ************************************/
		
		/*************************************
		 * Buttons Section
		 ************************************/

		BigButton cancelButton = new BigButton("Cancel");
		BigButton skipButton = new BigButton("Skip");
		BigButton replaceButton = new BigButton("Replace");
		Box buttonsBox = Box.createHorizontalBox();
		buttonsBox.add(cancelButton, BorderLayout.CENTER);
		buttonsBox.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		buttonsBox.add(skipButton, BorderLayout.CENTER);
		buttonsBox.add(replaceButton, BorderLayout.CENTER);
		
		this.registerButton(skipButton, SKIP);
		this.registerButton(replaceButton, REPLACE);
		this.registerButton(cancelButton, CANCEL);

		/*************************************
		 * End Buttons Section
		 ************************************/
		
		
		Insets insets = new Insets(5, 5, 5, 5);
		
		
		GUIHelpers.addComponent(frame, messagesBox, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets);
		GUIHelpers.addComponent(frame, filesBox, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets);
		GUIHelpers.addComponent(frame, renameToggleButton, 0, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, insets);
		GUIHelpers.addComponent(frame, renameBox, 0, 4, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);
		GUIHelpers.addComponent(frame, renameErrorLabel, 0, 5, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);
		GUIHelpers.addComponent(frame, buttonsBox, 0, 6, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets);

		
		frame.setSize(600, 300);
		frame.setVisible(true);
	}
	
	public String getInputValue() {
		try {
			return this.renamedTextDocument.getText(0, this.renamedTextDocument.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
}
