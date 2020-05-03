package components;

import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;

import javax.swing.JProgressBar;

import copy.SuperModel;
import gui.LongProgressBarModel;

public class ExtendedProgressBar extends JProgressBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1601083430707519699L;
	private SuperModel SM;
	
	private String leftString = "";
	private String rightString = "";
	
	public ExtendedProgressBar(LongProgressBarModel model) {
		super(model);
	}
	
	public void attachSM(SuperModel SM) {
		this.SM = SM;
	}
	
	public String getRightString() {
		return this.rightString;
	}
	
	public String getLeftString() {
		return this.leftString;
	}
	
	public void setRightString(String rightString) {
		this.rightString = rightString;
	}
	
	public void setLeftString(String leftString) {
		this.leftString = leftString;
	}
}
