package gui;
import javax.swing.*;
import javax.swing.event.ChangeListener;

import buffer.Buffer;
import buffer.StaticBuffer;
import copy.Copy;
import copy.SuperModel;
import copy.CopiableList;
import listeners.LongProgressBarListener;

import java.awt.*;
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
		JLabel fromLabel = new JLabel("Origin: " + this.orig, SwingConstants.LEFT);
		JLabel currentLabel = new JLabel("", SwingConstants.LEFT);
		LongProgressBarModel fileProgressModel = new LongProgressBarModel();
		LongProgressBarModel totalProgressModel = new LongProgressBarModel();
		
		
		JProgressBar fileCopyProgressBar = new JProgressBar(fileProgressModel);
		ChangeListener fileProgressListener = new LongProgressBarListener(fileCopyProgressBar);
		fileProgressModel.addChangeListener(fileProgressListener);

		currentLabel.setLabelFor(fileCopyProgressBar);
		
		JProgressBar totalCopyProgressBar = new JProgressBar(totalProgressModel);
		ChangeListener totalProgressListener = new LongProgressBarListener(totalCopyProgressBar);
		fileProgressModel.addChangeListener(totalProgressListener);
		
		fromLabel.setLabelFor(totalCopyProgressBar);

		fileCopyProgressBar.setStringPainted(true);
		totalCopyProgressBar.setStringPainted(true);
		
		addComponent(frame, fromLabel, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.CENTER);
		addComponent(frame, totalCopyProgressBar, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		
		addComponent(frame, currentLabel, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.CENTER);
		addComponent(frame, fileCopyProgressBar, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		
		frame.setSize(600, 200);
		frame.setResizable(false);
		frame.setVisible(true);
		
//		Thread copyThread = new CopyThread(copyProgressBar, copy);
		Copy copyThread;
//		UIElementsHolder holder = new UIElementsHolder(fileProgressModel, totalProgressModel, currentLabel);
		try {
			CopiableList copiableList = new CopiableList();
			Buffer buffer = new StaticBuffer();
			SuperModel SM = new SuperModel(copiableList, buffer);
			SM.setFileProgressModel(fileProgressModel);
			SM.setTotalProgressModel(totalProgressModel);
			SM.setCurrentLabel(currentLabel);
			SM.setFrame(frame);
			SM.setHasGUI(true);
			
			copyThread = new Copy(this.orig, this.dest, SM);
			
			copyThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
