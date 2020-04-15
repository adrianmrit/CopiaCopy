package gui;
import javax.swing.*;

import copy.Copy;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class CopyGUI implements Runnable{
	private static boolean DEBUG = false;
	
	private static final Insets insets = new Insets(3,3,3,3);
	
	private final String orig;
	private final String dest;
	
	public CopyGUI(String orig, String dest) {
		this.orig = orig;
		this.dest = dest;
	}
	
	private static void addComponent(Container container, Component component,
			int gridx, int gridy, int gridwidth, int gridheight, int anchor,
			int fill) {
			GridBagConstraints gbc = new GridBagConstraints(gridx, gridy,
			gridwidth, gridheight, 1.0, 1.0, anchor, fill, insets, 0, 0);
			container.add(component, gbc);
	}
	
	/** Sets the debug status
	 * @param debug status
	 */
	public static void setDebug(boolean debug) {
		CopyGUI.DEBUG = debug;
	}
	
	public static void setTheme(JFrame frame, String theme) {
		try {
			UIManager.setLookAndFeel(theme);
			SwingUtilities.updateComponentTreeUI(frame);
		} catch (Exception exception) {
			JOptionPane.showMessageDialog (
			frame, "Can't change look and feel",
			"Invalid PLAF", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void run() {
		final JFrame frame = new JFrame("Copy Sample");
		frame.setLayout(new GridBagLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTheme(frame, "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		JButton button = new JButton("Select Me");
		JLabel fromLabel = new JLabel("Origin: " + this.orig, SwingConstants.LEFT);
		JLabel currentLabel = new JLabel("", SwingConstants.LEFT);
		
		LongProgressBar fileCopyProgressBar = new LongProgressBar();
		currentLabel.setLabelFor(fileCopyProgressBar);
		LongProgressBar totalCopyProgressBar = new LongProgressBar();
		fromLabel.setLabelFor(totalCopyProgressBar);
		fileCopyProgressBar.setStringPainted(true);
		totalCopyProgressBar.setStringPainted(true);
		// Define ActionListener
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				System.out.println("I was selected.");
			}
		};
		// Attach listeners
		button.addActionListener(actionListener);
		addComponent(frame, fromLabel, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.CENTER);
		addComponent(frame, totalCopyProgressBar, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		
		addComponent(frame, currentLabel, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.CENTER);
		addComponent(frame, fileCopyProgressBar, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		
		frame.setSize(600, 200);
		frame.setResizable(false);
		frame.setVisible(true);
		
//		Thread copyThread = new CopyThread(copyProgressBar, copy);
		Copy copyThread;
		UIElementsHolder holder = new UIElementsHolder(fileCopyProgressBar, totalCopyProgressBar, currentLabel);
		try {
			copyThread = new Copy(this.orig, this.dest, holder);
			copyThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
