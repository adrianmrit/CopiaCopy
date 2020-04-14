package gui;

import javax.swing.JLabel;

public class UIElementsHolder {
	public final LongProgressBar fileProgressBar;
	public final LongProgressBar totalProgressBar;
	public final JLabel currentLabel;
	
	public UIElementsHolder(LongProgressBar fileProgressBar,
			LongProgressBar totalProgressBar, JLabel currentLabel) {
			this.fileProgressBar = fileProgressBar;
			this.totalProgressBar = totalProgressBar;
			this.currentLabel = currentLabel;
	}
}
