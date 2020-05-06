package gui;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
import net.miginfocom.swing.MigLayout;

public class ExistsDialog extends AbstractDialog{
	private File origin;
	private File dest;
	private String windowsTitle;
	private String title;
	private String message;
	private JPanel replaceWithBox;
	private JPanel originalBox;
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
		this.replaceWithBox = getFilePanel(f, "Replace with");
	}
	
	public void setOriginalBox(File f) {
		this.originalBox = getFilePanel(f, "Original");
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
	
	
	private JPanel getFilePanel(File f, String label) {
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
		final JPanel fileBox = new JPanel();
		fileBox.setLayout(new MigLayout("fill, hidemode 2"));

		Icon ico = IcoHelpers.loadFileIco(f);
		
		JLabel icoLabel = new JLabel(ico);
		
		fileBox.add(icoLabel, "span, align center");
		fileBox.add(origLabel, "span, align center");
		fileBox.add(origSizeLabel, "span, align center");
		fileBox.add(lastModLabel, "span, align center");
		fileBox.add(origModLabel, "span, align center");
		
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
		final JPanel content = new JPanel();
		LayoutManager layout = new MigLayout("gap 0 0 0 0, fill");
		content.setLayout(layout);
		this.getJDialog().setContentPane(content);
		
		/*************************************
		 * Message Section
		 ************************************/
		
		JLabel titleLabel = new JLabel(this.title);
		JLabel messageLabel = new JLabel(this.message);
		
//		Box messagesBox = Box.createVerticalBox();
//		messagesBox.add(titleLabel, BorderLayout.CENTER);
//		messagesBox.add(messageLabel, BorderLayout.CENTER);
		/*************************************
		 * End Message Section
		 ************************************/
		
		/*************************************
		 * Files Section
		 ************************************/
		
		JPanel replacePanel = getFilePanel(this.dest, "Replace with");
		JPanel origPanel = getFilePanel(this.origin, "Original");
		
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

		final JPanel renamePanel = new JPanel();
		renamePanel.setLayout(new MigLayout("fill, hidemode 2"));
		renamePanel.add(renameField, "gapleft push, width 60%!, height 30px!");
		renamePanel.add(renameButton, "gapright push, height 30px!");

		renamePanel.setVisible(false);
		
		ActionListener toggleRenameListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if(renamePanel.isVisible()) {
					renamePanel.setVisible(false);
					renameToggleButton.setIcon(icoUnnactive);
				} else {
					renamePanel.setVisible(true);
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
		final JPanel buttonsBox = new JPanel();
		buttonsBox.setLayout(new MigLayout("fill"));
		
		buttonsBox.add(cancelButton, "push 200");
		buttonsBox.add(skipButton);
		buttonsBox.add(mrButton);

		/*************************************
		 * End Buttons Section
		 ************************************/
		content.add(titleLabel, "dock north");
		content.add(messageLabel, "dock north");
		content.add(origPanel, "gapleft push, width 50%!");
		content.add(replacePanel, "gapright push, width 50%!, wrap");
		content.add(renameToggleButton, "span, align center");
		content.add(renamePanel, "span, width 100%!");
		content.add(buttonsBox, "span, dock south");
		
		setSize(600, 450);
		showDialog();
	}

}
