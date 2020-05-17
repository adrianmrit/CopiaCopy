package gui;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

import org.apache.commons.lang3.StringUtils;

import buffer.Buffer;
import buffer.StaticBuffer;
import components.DefaultButton;
import components.ExtendedProgressBar;
import components.ExtendedProgressBarUI;
import components.JTableWithToolTips;
import copy.CopiableList;
import copy.CopiableLoader;
import copy.Copy;
import enums.CopyMode;
import languages.LangBundle;
import listeners.ExtendedProgressBarListener;
import listeners.LongProgressBarListener;
import mdlaf.MaterialLookAndFeel;
import mdlaf.components.progressbar.MaterialProgressBarUI;
import mdlaf.themes.MaterialTheme;
import models.CopyQueueModel;
import models.ErrorsModel;
import models.ExtendedProgressBarModel;
import models.LongProgressBarModel;
import models.SuperModel;
import net.miginfocom.swing.MigLayout;
import themes.JMarsDarkCustom;
import themes.LiteCustom;

public class CopyGUI implements Runnable{
	private static boolean DEBUG = false;
	
	private static final int WINDOWS_HEIGHT = 190;
	private static final int WINDOWS_WIDTH = 500;
	private static final int MORE_INFO_HEIGHT = 250;
	
	private final String orig;
	private final String dest;
	private final CopyMode mode;
	
	public CopyGUI(String orig, String dest, CopyMode mode) {
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
		LangBundle.load(Locale.forLanguageTag("es-ES"));
		final JFrame frame = new JFrame();
		final JPanel content = new JPanel();
		LayoutManager layout = new MigLayout("gap 0 0 0 0, fill, hidemode 3");
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
			MaterialTheme theme = new JMarsDarkCustom();
			UIManager.setLookAndFeel (new MaterialLookAndFeel (theme));
			content.setBackground(theme.getBackgroundPrimary());
		}
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace ();
		}
		
		JLabel nameLabel = new JLabel(" ", SwingConstants.LEFT);
		JLabel infoLabel = new JLabel(" ", SwingConstants.LEFT);
		JLabel originLabel = new JLabel(" ", SwingConstants.LEFT);
		JLabel destinationLabel = new JLabel(" ", SwingConstants.LEFT);
		
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
		
		JToggleButton moreButton = new JToggleButton( LangBundle.CURRENT.getString("more") );
		JButton pauseButton = new DefaultButton( LangBundle.CURRENT.getString("pause") );
		JButton skipButton = new DefaultButton( LangBundle.CURRENT.getString("skip") );
		JButton cancelButton = new DefaultButton( LangBundle.CURRENT.getString("cancel") );

		final JPanel rightContent = new JPanel();
		rightContent.setLayout(new MigLayout("gap 0 0 0 0, fill"));
		rightContent.add(nameLabel, "span");
		rightContent.add(fileCopyProgressBar, "span, width 100%-5!, pad 0 -5 0 0, gapx 0 0, height 30px!");
		rightContent.add(infoLabel, "span");
		rightContent.add(originLabel, "span");
		rightContent.add(destinationLabel, "span");
		
		final JPanel bottomContent = new JPanel();
		bottomContent.setLayout(new MigLayout("fill"));
		bottomContent.add(moreButton, "push 200");
		bottomContent.add(pauseButton, "");
		bottomContent.add(skipButton, "");
		bottomContent.add(cancelButton, "");
		
		/**
		 * Queue Table Section 
		 **/
		
		final JPanel queueContent = new JPanel();
		queueContent.setLayout(new MigLayout("fill, gap 0 0 0 0"));
		CopyQueueModel copyQueueModel = new CopyQueueModel();
		
		JTable fileQueueTable = new JTableWithToolTips(copyQueueModel);
		JScrollPane fileQueueScrollPane = new JScrollPane(fileQueueTable);
		JButton skipSelected = new DefaultButton( LangBundle.CURRENT.getString("skip selected") );
		
		queueContent.add(fileQueueScrollPane, "span, width 100%-14!");
		queueContent.add(skipSelected, "gapleft push, gaptop 5");
		
		/**
		 * Queue Table Section END
		 **/
		
		/**
		 * Errors Table Section 
		 **/
		
		final JPanel errorsContent = new JPanel();
		errorsContent.setLayout(new MigLayout("fill, gap 0 0 0 0"));
		ErrorsModel errorsListModel = new ErrorsModel();
		JTable fileErrorsTable = new JTableWithToolTips(errorsListModel);
		
		JScrollPane errorsContentScrollPane = new JScrollPane(fileErrorsTable);
		errorsContent.add(errorsContentScrollPane, "span, width 100%-14!");
		
		/**
		 * Errors Table Section END
		 **/
		
		
		JTabbedPane bottomTabs = new JTabbedPane();
		bottomTabs.add(LangBundle.CURRENT.getString("queueTab"), queueContent);
		bottomTabs.add(LangBundle.CURRENT.getString("errorsTab"), errorsContent);
		
		
		content.add(bottomTabs, "dock south, width 100%!");
		content.add(bottomContent, "dock south, width 100%!");
		content.add(totalCopyProgressBar, "dock west, width 17%, gapx 3 0, gapy 3 push, height 100px!");
		content.add(rightContent, "dock north, width 83%!, gapx 0 3");
		
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
		SM.setOriginLabel(originLabel);
		SM.setDestinationLabel(destinationLabel);
		SM.setCurrentLabel(nameLabel);
		SM.setQueueModel(copyQueueModel);
		SM.setErrorListModel(errorsListModel);
		SM.setFrame(frame);
		SM.setHasGUI(true);
		
		ActionListener pauseListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				SM.togglePaused();
				if (SM.isPaused()) {
					pauseButton.setText(LangBundle.CURRENT.getString("resume"));
				} else {
					pauseButton.setText(LangBundle.CURRENT.getString("pause"));
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
		
		ActionListener skipSelectedListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				SM.togglePaused(); // avoids starting a new copy while deleting rows

				copyQueueModel.skip(fileQueueTable.getSelectedRows());
				
				SM.togglePaused();// continues the copy
			}
		};
		
		ActionListener moreListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				JToggleButton toggleButton = (JToggleButton) actionEvent.getSource();
				if (toggleButton.isSelected()) {
					bottomTabs.setVisible(true);
					skipSelected.setEnabled(true);
					frame.setSize(new Dimension(WINDOWS_WIDTH, WINDOWS_HEIGHT + MORE_INFO_HEIGHT));
				} else {
					bottomTabs.setVisible(false);
					skipSelected.setEnabled(false);
					frame.setSize(new Dimension(WINDOWS_WIDTH, WINDOWS_HEIGHT));
				}
				
			}
		};
		
		queueContent.setVisible(false);
		
		moreButton.addActionListener(moreListener);
		pauseButton.addActionListener(pauseListener);
		cancelButton.addActionListener(cancelListener);
		skipButton.addActionListener(skipListener);
		skipSelected.addActionListener(skipSelectedListener);
		
		copyThread = new Copy(SM);
		CopiableLoader loader = new CopiableLoader(SM, this.orig, this.dest, this.mode);
		copyThread.addToCopy(loader);
		copyThread.execute();
		try {
			copyThread.get();
		} catch (InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
