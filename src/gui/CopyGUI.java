package gui;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeListener;

import buffer.Buffer;
import buffer.StaticBuffer;
import components.DefaultButton;
import components.ExtendedProgressBar;
import components.ExtendedProgressBarUI;
import copy.CopiableList;
import copy.CopiableLoader;
import copy.Copy;
import copy.SuperModel;
import listeners.ExtendedProgressBarListener;
import listeners.LongProgressBarListener;
import mdlaf.MaterialLookAndFeel;
import mdlaf.components.progressbar.MaterialProgressBarUI;
import mdlaf.themes.MaterialTheme;
import net.miginfocom.swing.MigLayout;
import themes.LiteCustom;

public class CopyGUI implements Runnable{
	private static boolean DEBUG = false;
	
	private static final int WINDOWS_HEIGHT = 180;
	private static final int WINDOWS_WIDTH = 500;
	
	private final String orig;
	private final String dest;
	private final int mode;
	
	public CopyGUI(String orig, String dest, int mode) {
		this.orig = orig;
		this.dest = dest;
		this.mode = mode;
	}
	
	/** Sets the debug status
	 * @param debug status
	 */
	public static void setDebug(boolean debug) {
		CopyGUI.DEBUG = debug;
	}
	
	public void run() {
		final JFrame frame = new JFrame("Copy");
		final JPanel content = new JPanel();
		LayoutManager layout = new MigLayout("gap 0 0 0 0, fill");
		content.setLayout(layout);
		frame.setContentPane(content);
		
		// avoids
		// Exception in thread "main" java.lang.ClassCastException: java.lang.Integer cannot be cast to java.awt.Dimension
		// DO NOT REMOVE NEXT LINE
		UIManager.put("ProgressBar.verticalSize", new Dimension(12, 146)); 		
		// DO NOT REMOVE PREVIOUS LINE
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			JDialog.setDefaultLookAndFeelDecorated(true);
			MaterialTheme theme = new LiteCustom();
			UIManager.setLookAndFeel (new MaterialLookAndFeel (theme));
			content.setBackground(theme.getBackgroundPrimary());
		}
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace ();
		}	
		
		JLabel nameLabel = new JLabel(" ", SwingConstants.LEFT);
		JLabel infoLabel = new JLabel(" ", SwingConstants.LEFT);
		
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
		
		JButton moreButton = new DefaultButton("More");
		JButton pauseButton = new DefaultButton("Pause");
		JButton skipButton = new DefaultButton("Skip");
		JButton cancelButton = new DefaultButton("Cancel");

		final JPanel rightContent = new JPanel();
		rightContent.setLayout(new MigLayout());
		rightContent.add(nameLabel, "span");
		rightContent.add(fileCopyProgressBar, "span, width 100%-6!, height 40px!");
		rightContent.add(infoLabel, "span");
		
		final JPanel bottomContent = new JPanel();
		bottomContent.setLayout(new MigLayout("fill"));
		bottomContent.add(moreButton, "push 200");
		bottomContent.add(pauseButton, "");
		bottomContent.add(skipButton, "");
		bottomContent.add(cancelButton, "");
		
		content.add(bottomContent, "dock south, width 100%!");
		content.add(totalCopyProgressBar, "dock west, gapx 6 0, gaptop 6");
		content.add(rightContent, "dock north, width 80%+7!, gapx 0 0");
		
		
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
		SM.setCurrentLabel(nameLabel);
		SM.setFrame(frame);
		SM.setHasGUI(true);
		
		ActionListener pauseListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				SM.togglePaused();
				if (SM.isPaused()) {
					pauseButton.setText("Resume");
				} else {
					pauseButton.setText("Pause");
				}
			}
		};
		
		ActionListener cancelListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				System.exit(0);
			}
		};
		
		ActionListener skipListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				SM.skipCurrent();
			}
		};
		
		pauseButton.addActionListener(pauseListener);
		cancelButton.addActionListener(cancelListener);
		skipButton.addActionListener(skipListener);
		
		copyThread = new Copy(SM);
		CopiableLoader loader = new CopiableLoader(SM, this.orig, this.dest, this.mode);
		copyThread.addToCopy(loader);
		copyThread.doInBackground();
	}
}
