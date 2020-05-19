package gui;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import languages.LangBundle;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialTheme;
import net.miginfocom.swing.MigLayout;
import themes.JMarsDarkCustom;

public class SettingsGUI {
	private static JPanel getLangPanel() {
		final JPanel langPanel = new JPanel();
		langPanel.setLayout(new MigLayout("gap 0 0 0 0"));
		
		final JLabel langPickerLabel = new JLabel(LangBundle.CURRENT.getString("langPickerLabel"));
		final JComboBox<String> langPicker = new JComboBox<String>(LangBundle.INSTALLED);
		langPickerLabel.setLabelFor(langPicker);
		langPanel.add(langPickerLabel, "gap 10 10 10 10");
		langPanel.add(langPicker, "wrap, gap 10 10 10 10");
		
		return langPanel;
	}
	
	private static JPanel getLookAndFeelPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("gap 0 0 0 0"));
		
		final JLabel themePickerLabel = new JLabel(LangBundle.CURRENT.getString("themePickerLabel"));
		final JComboBox<String> themePicker = new JComboBox<String>(new String[] {"JMarsDark", "MatrialLite", "Material", "MaterialOceanic"});
		
		panel.add(themePickerLabel, "gap 10 10 10 10");
		panel.add(themePicker, "wrap, gap 10 10 10 10");
		
		return panel;
	}
	
	private static JTabbedPane getTabs() {
		final JTabbedPane tabs = new JTabbedPane();
		tabs.add(LangBundle.CURRENT.getString("langTab"), getLangPanel());
		tabs.add(LangBundle.CURRENT.getString("lookAndFeelTab"), getLookAndFeelPanel());
		
		return tabs;
	}
	
	public static void main(String[] args) {
		LangBundle.load(LangBundle.ES_LOCALE);
		final JFrame frame = new JFrame();
		final JPanel content = new JPanel();
		LayoutManager layout = new MigLayout("gap 0 0 0 0, fill, hidemode 3");
		content.setLayout(layout);
		
		frame.setContentPane(content);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(LangBundle.CURRENT.getString("settingsTitle"));
		
		try {
			JDialog.setDefaultLookAndFeelDecorated(true);
			MaterialTheme theme = new JMarsDarkCustom();
			UIManager.setLookAndFeel (new MaterialLookAndFeel (theme));
			content.setBackground(theme.getBackgroundPrimary());
		}
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace ();
		}
		
		content.add(getTabs(), "dock north, width 100%!, height 100%!");
		// avoids
		// Exception in thread "main" java.lang.ClassCastException: java.lang.Integer cannot be cast to java.awt.Dimension
		// DO NOT REMOVE NEXT LINE
		UIManager.put("ProgressBar.verticalSize", new Dimension(12, 146)); 		
		// DO NOT REMOVE PREVIOUS LINE
		frame.setSize(new Dimension(500, 400));
		frame.setResizable(false);
		frame.setVisible(true);
	}

}
