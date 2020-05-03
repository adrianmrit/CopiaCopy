package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

import components.DefaultButton;
import components.UnstyledButton;
import copy.SizeRep;
import icons.ArrowIconBottom;
import icons.ArrowIconRight;
import icons.IcoHelpers;
import inputFilters.FileNameInputFilter;

public class ExistsDialog extends AbstractDialog{
	private File origin;
	private File dest;
	private String windowsTitle;
	private String title;
	private String message;
	private Box replaceWithBox;
	private Box originalBox;
	private Document renamedTextDocument;
	private final String RENAME_TOGGLE_MESSAGE = "<HTML><p><b>Select a new name for this destination</b></p></HTML>";
	private final String RENAME_BUTTON_MESSAGE = "Rename";
	private JButton cancelButton;
	private JButton renameButton;
	private JButton skipButton;
	private JButton mrButton; // merge or replace

	public ExistsDialog(JFrame parent, File origin, File dest) {
		super(parent);
		this.origin = origin;
		this.dest = dest;
	}
	
	public File getOrigin() {
		return this.origin;
	}
	
	public File getDest() {
		return this.dest;
	}
	
	public void setWindowsTitle(String title) {
		this.windowsTitle = title; 
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setReplaceWithBox(File f) {
		this.replaceWithBox = getFileBox(f, "Replace with");
	}
	
	public void setOriginalBox(File f) {
		this.originalBox = getFileBox(f, "Original");
	}
	
	public void setCancelButton(String name, String action) {
		cancelButton = new DefaultButton(name);
		registerButton(cancelButton, action);
	}
	
	public void setRenameButton(String name, String action) {
		renameButton = new DefaultButton(name);
		renameButton.setEnabled(false);
		registerButton(renameButton, action);
	}
	
	public void setSkipButton(String name, String action) {
		skipButton = new DefaultButton(name);
		registerButton(skipButton , action);
	}
	
	public void setMRButton(String name, String action) {
		mrButton = new DefaultButton(name);
		registerButton(mrButton , action);
	}
	
	
	private Box getFileBox(File f, String label) {
		String origSizeString;
		if (f.isFile()) {
			double fileSize = SizeRep.readableVal(f.length());
			String fileSizeRep = SizeRep.readableRep(f.length());
			
			origSizeString = String.format("Size: %.2f%s", fileSize, fileSizeRep);
		} else {
			String[] content = f.list();
			int contentTotal = 0;
			if (content != null) {
				contentTotal = content.length;
			}
			String s = "";
			if (contentTotal != 1) {
				s = "s";
			}
			
			origSizeString = String.format("Contains %d item%s", contentTotal, s);
		}
		
		Date lastMod = new Date(f.lastModified());
		JLabel origLabel = new JLabel(label);
		
		JLabel origSizeLabel = new JLabel(origSizeString);
		JLabel lastModLabel = new JLabel("Last modified:");
		JLabel origModLabel = new JLabel(String.format("%1$tF %1$tr", lastMod));
		
		// Origin file container
		Box fileBox = Box.createVerticalBox();
		Icon ico = IcoHelpers.loadFileIco(f);
		
		JLabel icoLabel = new JLabel(ico);
		
		fileBox.add(icoLabel, BorderLayout.CENTER);
		fileBox.add(origLabel, BorderLayout.CENTER);
		fileBox.add(origSizeLabel, BorderLayout.CENTER);
		fileBox.add(lastModLabel, BorderLayout.CENTER);
		fileBox.add(origModLabel, BorderLayout.CENTER);
		
		return fileBox;
	}
	
	public String getInputValue() {
		try {
			return this.renamedTextDocument.getText(0, this.renamedTextDocument.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@Override
	public String getAction() {
		this.getParent().setEnabled(true);
		return super.getAction();
	}
	
	public void show() {
		
		this.getParent().setEnabled(false);
		this.setJDialog(new JDialog(this.getParent(), windowsTitle, true));
		this.getJDialog().setLayout(new GridBagLayout());
		this.getJDialog().setResizable(false);
		this.getJDialog().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		/*************************************
		 * Message Section
		 ************************************/
		
		JLabel titleLabel = new JLabel(this.title);
		JLabel messageLabel = new JLabel(this.message);
		
		Box messagesBox = Box.createVerticalBox();
		messagesBox.add(titleLabel, BorderLayout.CENTER);
		messagesBox.add(messageLabel, BorderLayout.CENTER);
		/*************************************
		 * End Message Section
		 ************************************/
		
		/*************************************
		 * Files Section
		 ************************************/
		
		Box replaceBox = getFileBox(this.dest, "Replace with");
		Box origBox = getFileBox(this.origin, "Original");
		
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
		JButton renameToggleButton = new UnstyledButton(RENAME_TOGGLE_MESSAGE);
		
		int icoHeight = 8;
		int icoWidth = 5;
		Icon icoUnnactive = new ArrowIconRight(new Color(0, 0, 0), icoWidth, icoHeight);
		renameToggleButton.setIcon(icoUnnactive);
		
		Icon icoActive = new ArrowIconBottom(new Color(0, 0, 0), icoHeight, icoWidth);  // exchange sizes
		
		JTextField renameField = new JTextField(this.dest.getName());
		JLabel renameErrorLabel = new JLabel("");
		renameErrorLabel.setVisible(false);

		renameField.setMargin(new Insets(0,0,0,0));
		Box renameBox = Box.createHorizontalBox();
		renameBox.add(Box.createRigidArea(new Dimension(100, 0)), BorderLayout.CENTER);
		renameBox.add(renameField, BorderLayout.CENTER);
		renameBox.add(renameButton, BorderLayout.CENTER);
		renameBox.add(Box.createRigidArea(new Dimension(100, 0)), BorderLayout.CENTER);

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

		this.renamedTextDocument = renameField.getDocument();
		DocumentFilter fileNameFilter = new FileNameInputFilter(renameErrorLabel, renameButton, renameField.getText());
		((AbstractDocument)renamedTextDocument).setDocumentFilter(fileNameFilter);
		/*************************************
		 * End Rename Section
		 ************************************/
		
		/*************************************
		 * Buttons Section
		 ************************************/

		Box buttonsBox = Box.createHorizontalBox();
		buttonsBox.add(cancelButton, BorderLayout.CENTER);
		buttonsBox.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		buttonsBox.add(skipButton, BorderLayout.CENTER);
		buttonsBox.add(mrButton, BorderLayout.SOUTH);

		/*************************************
		 * End Buttons Section
		 ************************************/
		
		Insets insets = new Insets(20, 20, 20, 20);
		
		GUIHelpers.addComponent(this.getJDialog(), messagesBox, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);
		GUIHelpers.addComponent(this.getJDialog(), filesBox, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);
		GUIHelpers.addComponent(this.getJDialog(), renameToggleButton, 0, 2, 1, 1, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, insets);
		GUIHelpers.addComponent(this.getJDialog(), renameBox, 0, 3, 1, 1, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, insets);
		GUIHelpers.addComponent(this.getJDialog(), renameErrorLabel, 0, 4, 1, 1, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, insets);
		GUIHelpers.addComponent(this.getJDialog(), buttonsBox, 0, 5, 1, 1, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, insets);
		
		setSize(700, 500);
		showDialog();
	}

}
