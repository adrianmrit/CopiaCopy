package gui;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalProgressBarUI;
import javax.swing.plaf.metal.MetalTheme;

import org.apache.commons.lang3.StringUtils;

import buffer.Buffer;
import buffer.StaticBuffer;
import components.ExtendedProgressBar;
import components.ExtendedProgressBarUI;
import copy.Copy;
import copy.SuperModel;
import copy.Copiable;
import copy.CopiableList;
import listeners.ExtendedProgressBarListener;
import listeners.LongProgressBarListener;
import mdlaf.MaterialLookAndFeel;
import mdlaf.components.progressbar.MaterialProgressBarUI;
import mdlaf.themes.JMarsDarkTheme;
import mdlaf.themes.MaterialLiteTheme;
import mdlaf.themes.MaterialOceanicTheme;
import mdlaf.themes.MaterialTheme;

import java.awt.*;
import java.io.IOException;

public class CopyGUI implements Runnable{
	private static boolean DEBUG = false;
	
	private static final Insets insets = new Insets(0,0,0,0);
	private static final int WINDOWS_HEIGHT = 200;
	private static final int WINDOWS_WIDTH = 600;
	
	private final String orig;
	private final String dest;
	private final int mode;
	
	public CopyGUI(String orig, String dest, int mode) {
		this.orig = orig;
		this.dest = dest;
		this.mode = mode;
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
	
	public void run() {
		final JFrame frame = new JFrame("Copy Sample");
		final JPanel content = new JPanel();
		LayoutManager layout = new BoxLayout (content, BoxLayout.Y_AXIS);
		content.setLayout(layout);
		frame.setContentPane(content);
		UIManager.put("ProgressBar.verticalSize", new Dimension(12, 146));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			JDialog.setDefaultLookAndFeelDecorated(true);
			MaterialTheme theme = new MaterialLiteTheme();
			UIManager.setLookAndFeel (new MaterialLookAndFeel (theme));
			content.setBackground(theme.getBackgroundPrimary());
		}
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace ();
		}	
		
		JLabel nameLabel = new JLabel("name: Example Name", SwingConstants.LEFT);
		JLabel infoLabel = new JLabel("copied: 100/200 (200MB/3GB)", SwingConstants.LEFT);
		ExtendedProgressBarModel fileProgressModel = new ExtendedProgressBarModel();
		LongProgressBarModel totalProgressModel = new LongProgressBarModel();
		
		
		ExtendedProgressBar fileCopyProgressBar = new ExtendedProgressBar(fileProgressModel);
		fileCopyProgressBar.setUI(new ExtendedProgressBarUI(fileCopyProgressBar));
		ChangeListener fileProgressListener = new LongProgressBarListener(fileCopyProgressBar);
		ChangeListener extendedProgressListener = new ExtendedProgressBarListener(fileCopyProgressBar);
		fileProgressModel.addChangeListener(fileProgressListener);
		fileProgressModel.addChangeListener(extendedProgressListener);
		

		infoLabel.setLabelFor(fileCopyProgressBar);
		
		JProgressBar totalCopyProgressBar = new JProgressBar(JProgressBar.VERTICAL) {
			@Override
			public int getOrientation() {
				for(StackTraceElement elem: new Throwable().getStackTrace()) {
					if(elem.getMethodName().equals("paintText") || (elem.getMethodName().equals("paintString"))) {
						return JProgressBar.HORIZONTAL;
					}
				}
				return JProgressBar.VERTICAL;
			}
		};
		
		
		totalCopyProgressBar.setModel(totalProgressModel);
		ChangeListener totalProgressListener = new LongProgressBarListener(totalCopyProgressBar);
		totalProgressModel.addChangeListener(totalProgressListener);
		
		nameLabel.setLabelFor(totalCopyProgressBar);

		fileCopyProgressBar.setStringPainted(true);
		totalCopyProgressBar.setStringPainted(true);
		
		JButton moreButton = new JButton("More");
		JButton pauseButton = new JButton("Pause");
		JButton skipButton = new JButton("Skip");
		JButton cancelButton = new JButton("Cancel");
		
		Box buttonsBox = Box.createHorizontalBox();
		buttonsBox.add(moreButton, BorderLayout.CENTER);
		buttonsBox.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		buttonsBox.add(pauseButton, BorderLayout.CENTER);
		buttonsBox.add(skipButton, BorderLayout.CENTER);
		buttonsBox.add(cancelButton, BorderLayout.CENTER);
		
		Box labelsBox = Box.createVerticalBox();
		labelsBox.add(Box.createVerticalStrut(5));
		labelsBox.add(infoLabel, BorderLayout.CENTER);
		labelsBox.add(Box.createVerticalStrut(5));
		
		JLabel sizeCopiedLabel = new JLabel("200mb/1GB", SwingConstants.LEFT);
		JLabel timeLeftLabel = new JLabel("00:01:59", SwingConstants.RIGHT);
		sizeCopiedLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		timeLeftLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		
		JPanel labelsAndHPBarPanel = new JPanel();
		labelsAndHPBarPanel.setLayout(new GridBagLayout());
		addComponent(labelsAndHPBarPanel, nameLabel, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		
		addComponent(labelsAndHPBarPanel, fileCopyProgressBar, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		
		addComponent(labelsAndHPBarPanel, labelsBox, 0, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		labelsAndHPBarPanel.setPreferredSize(new Dimension(WINDOWS_WIDTH/3, 0));
		
		JLabel totalCopiedLabel = new JLabel("200mb/20GB", SwingConstants.CENTER);
		
		
		
		Box topContent = Box.createHorizontalBox();
		topContent.add(totalCopyProgressBar);
		topContent.add(Box.createHorizontalStrut(5));
		topContent.add(labelsAndHPBarPanel);
		
		content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		content.add(topContent, BorderLayout.CENTER);
		content.add(Box.createVerticalStrut(5));
		content.add(buttonsBox, BorderLayout.CENTER);
		
		
		totalCopyProgressBar.setPreferredSize(new Dimension(WINDOWS_WIDTH/6, 0));
		totalCopyProgressBar.setUI(new MaterialProgressBarUI());
		
		
		frame.setSize(new Dimension(WINDOWS_WIDTH, WINDOWS_HEIGHT));
		frame.setResizable(false);
		frame.setVisible(true);
		
		Copy copyThread;
		CopiableList copiableList = new CopiableList();
		Buffer buffer = new StaticBuffer();
		SuperModel SM = new SuperModel(copiableList, buffer);
		SM.setFileProgressModel(fileProgressModel);
		SM.setTotalProgressModel(totalProgressModel);
		SM.setInfoLabel(infoLabel);
		SM.setToLabel(nameLabel);
		SM.setFrame(frame);
		SM.setHasGUI(true);
		
		copyThread = new Copy(SM);
		copyThread.addToCopy(this.orig, this.dest, this.mode);
		copyThread.start();
	}
}
