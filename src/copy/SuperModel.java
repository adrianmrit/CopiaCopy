package copy;

import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;

import gui.LongProgressBarModel;

public class SuperModel {
	public CopiableList copiableList;
	public DinamicBuffer dinamicBuffer;
	
	public LongProgressBarModel fileProgressModel;
	public LongProgressBarModel totalProgressModel;
	public JLabel currentLabel;
	public JFrame frame; // used to create windows
	private boolean hasGUI = false;
	
	public SuperModel(CopiableList cl, DinamicBuffer dinb) {
		this.copiableList = cl;
		this.dinamicBuffer = dinb;
	}
	
	/** 
	 * Sets the frame. Useful for creating dialogs.
	 * @param frame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	/** 
	 * Sets the file progress model
	 * @param fileProgressModel
	 */
	public void setFileProgressModel(LongProgressBarModel fileProgressModel) {
		this.fileProgressModel = fileProgressModel;
	}
	
	/**
	 * Sets the totalProgressModel
	 * @param totalProgressModel
	 */
	public void setTotalProgressModel(LongProgressBarModel totalProgressModel) {
		this.totalProgressModel = totalProgressModel;
	}
	
	/** 
	 * Sets the currentLabel
	 * @param currentLabel
	 */
	public void setCurrentLabel(JLabel currentLabel) {
		this.currentLabel = currentLabel;
	}
	
	/** 
	 * Set hasGUI. If there is GUI it will be updated
	 * @param b
	 */
	public void setHasGUI(boolean b) {
		this.hasGUI = b;
	}
	
	public boolean hasGUI() {
		return this.hasGUI;
	}
}
