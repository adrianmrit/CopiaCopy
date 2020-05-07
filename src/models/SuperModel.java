package models;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.table.TableModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import buffer.Buffer;
import copy.Copiable;
import copy.CopiableList;
import enums.ConflictAction;
import utils.TimerFormater;

/**
 * A model that holds the status of the copy and other info,
 * and participates as an intermediate between the copy and the GUI
 * @author adrianmrit
 *
 */
public class SuperModel {
	public CopiableList copiableList;
	public Buffer buffer;
	
	public ExtendedProgressBarModel fileProgressModel;
	public LongProgressBarModel totalProgressModel;
	public JLabel nameLabel;
	public JLabel infoLabel;
	public JFrame frame; // used to create windows

	private int copiedFiles;
	
	
	private long totalCopiedSize=0;
	
	private long currentTotalSize;
	private long currentCopiedSize = 0;
	
	private long totalTimeLeft;
	private long currentTimeLeft;
	private long speed;
	private long lastGUIUpdate;
	
	private boolean calculatingTimeLeft = true;
	
	private boolean paused = false;
	
	private boolean hasGUI = false;
		
	/**
	 * Update speed in milliseconds for some GUI items.
	 * It might take longer to update, but not less.
	 */
	public final int UPDATE_SPEED = 200; //Ms
	private CopyQueueModel copyQueueModel;
	
	/**
	 * Model that hold some data and updates the GUI
	 * @param cl Copiable list that hold the files to be copied
	 * @param buffer Buffer used to copy the files
	 */
	public SuperModel(CopiableList cl, Buffer buffer) {
		this.copiableList = cl;
		this.buffer = buffer;
	}
	
	/**
	 * Gets whether the copy is paused or not.
	 * @return true if the copy is paused, false otherwise
	 */
	public boolean isPaused() {
		return this.paused;
	}
	
	/**
	 * Toggles the pause status
	 */
	public void togglePaused() {
		this.paused = !this.paused;
	}
	
	/**
	 * Pauses the copy
	 */
	public void pause() {
		this.paused = true;
	}
	
	/**
	 * Continues the copy
	 */
	public void play() {
		this.paused = false;
	}
	
	/**
	 * Calculates the elapsed time from the last call of this function.
	 * @return true if elapsed time is greater than UPDATE_SPEED, false otherwise.
	 */
	public boolean shouldUpdate() {
		boolean update;
		long timeNow = System.currentTimeMillis();
		if (timeNow - this.lastGUIUpdate > UPDATE_SPEED) {
			update = true;
			this.lastGUIUpdate = timeNow;
		} else {
			update = false;
		}
		return update;
	}
	
	/** 
	 * Sets the frame. Useful for creating dialogs.
	 * @param frame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
		this.lastGUIUpdate = System.currentTimeMillis();
	}
	
	/**
	 * Sets the table model for the copy queue table
	 * @param copyQueueModel
	 */
	public void setQueueModel(TableModel copyQueueModel) {
		this.copyQueueModel = (CopyQueueModel) copyQueueModel;
	}
	
	/**
	 * Inserts a copiable into the copy queue table
	 * @param c
	 */
	public void insertCopyQueue(Copiable c) {
		if (hasGUI()) {
			this.copyQueueModel.insert(c);
		}
	}
	
	/**
	 * Removes a copiable from the copy queue table
	 * @param c
	 */
	public void removeCopyQueue(Copiable c) {
		this.pause();
		if (hasGUI()) {
			this.copyQueueModel.remove(c);
		}
		this.copiableList.remove(c);
		this.play();
	}
	
	/**
	 * Updates the information label
	 */
	private void updateInfoLabel() {
		if(this.hasGUI()) {
			String totalSizeString = FileUtils.byteCountToDisplaySize(this.copiableList.getTotalSize());
			String copiedSizeString = FileUtils.byteCountToDisplaySize(this.totalCopiedSize);
			String formatString = "%d/%d files - %s/%s, %s, %s";
			String info = String.format(formatString, this.copiableList.getNaturalCurrentCopy(), this.copiableList.getCount(), copiedSizeString, totalSizeString, getSpeed(), getTotalTimeLeft());
			if (infoLabel.getText() != info) {
				infoLabel.setText(info);
			}
			updateFrameTitle();
		}
	}
	
	/**
	 * Updates the frame title
	 */
	private void updateFrameTitle() {
		if(this.hasGUI()) {
			int percent = totalProgressModel.getValue();
			String totalSizeString = FileUtils.byteCountToDisplaySize(this.copiableList.getTotalSize());
			String copiedSizeString = FileUtils.byteCountToDisplaySize(this.totalCopiedSize);
			String formatString = "%d%% - %d/%d - %s/%s, %s";
			String info = String.format(formatString, percent, this.copiableList.getNaturalCurrentCopy(), this.copiableList.getCount(), copiedSizeString, totalSizeString, getTotalTimeLeft());
			if (frame.getTitle() != info) {
				frame.setTitle(info);
			}
		}
	}
	
	/**
	 * Sets the current copy speed.
	 * @param speed current speed in seconds
	 */
	public void setSpeed(double speed) {
		if (shouldUpdate()) {
			this.speed = (long) speed;
			updateInfoLabel();
		}
	}
	
	/**
	 * Gets a readable representation of the copy speed.
	 * @return readable speed
	 */
	public String getSpeed() {
		String displaySize = FileUtils.byteCountToDisplaySize(speed);
		return String.format("%s/s", displaySize);
	}
	
	/**
	 * Increase the copied files counter.
	 */
	public void addCopiedFile() {
		this.copiedFiles++;
		updateInfoLabel();
	}
	
	/**
	 * Gets the total size, as in {@link CopiableList#getTotalSize()}.
	 * @return total size in bytes of files copied
	 */
	public long getTotalSize() {
		return this.copiableList.getTotalSize();
	}
	
	/**
	 * Gets the total data copied size.
	 * @return total copied size in bytes
	 */
	public long getTotalCopiedSize() {
		return this.totalCopiedSize;
	}
	
	/**
	 * Gets the total amount of data left to finish the copy.
	 * @return
	 */
	public long getTotalSizeLeft() {
		return this.getTotalSize() - this.getTotalCopiedSize();
	}
	
	/**
	 * Updates total copied size.
	 * @param size Size of last chunk of data copied
	 */
	public void addTotalCopiedSize(long size) {
		this.totalCopiedSize += size;
		this.totalProgressModel.addLongValue(size);
		updateInfoLabel();
	}
	
	/**
	 * Sets the current copy size, and updates the current copied size.
	 * @param size of current copied file
	 */
	public void setCurrentTotalSize(long size) {
		this.currentTotalSize = size;
		this.currentCopiedSize = 0; // resets copied size;
	}
	
	/**
	 * Sets the copied size for the current file.
	 * @param size Size of the last chunk of data copied.
	 */
	public void setCurrentCopiedSize(long size) {
		this.currentCopiedSize += size;
		String formatString = "%s/%s";
		String displayCopied = FileUtils.byteCountToDisplaySize(this.currentCopiedSize);
		String displayTotal = FileUtils.byteCountToDisplaySize(this.currentTotalSize);
		String sizeLeft = String.format(formatString, displayCopied, displayTotal);
		this.fileProgressModel.setLongValue(this.currentCopiedSize);
		this.fileProgressModel.setSizeLeft(sizeLeft);
	}
	
	/**
	 * Sets the expected time left to finish the copy of all files.
	 * @param time time left in seconds
	 */
	public void setTotalTimeLeft(long time) {
		time += (this.copiableList.getCount() - this.copiedFiles)/100;  // add one second per 100 remaining files
		if (this.totalTimeLeft != time) {
			this.totalTimeLeft = time;
			updateInfoLabel();
		}
	}
	
	/**
	 * Gets a readable string representation of the expected time left
	 * to finish the copy of all files.
	 * @return time in 00:00:00 format
	 */
	public String getTotalTimeLeft() {
		if (calculatingTimeLeft){
			return "calculating";
		}
		TimerFormater tFormater = new TimerFormater(this.totalTimeLeft);
		return tFormater.toString();
	}
	
	/**
	 * Set the calculation status for the time left
	 * @param calculating
	 */
	public void setCalculatingTimeLeft(boolean calculating) {
		this.calculatingTimeLeft = calculating;
	}
	
	/**
	 * Sets the time left to finish the copy of the current file.
	 * @param time time left in seconds
	 */
	public void setCurrentTimeLeft(long time) {
		if (this.currentTimeLeft != time) {
			this.currentTimeLeft = time;
			TimerFormater tFormater = new TimerFormater(this.currentTimeLeft);
			
			this.fileProgressModel.setTimeLeft(tFormater.toString());
		}
	}
	
	/** 
	 * Sets the file progress model
	 * @param fileProgressModel
	 */
	public void setFileProgressModel(ExtendedProgressBarModel fileProgressModel) {
		this.fileProgressModel = fileProgressModel;
	}
	
	/**
	 * Sets the totalProgressModel
	 * @param totalProgressModel
	 */
	public void setTotalProgressModel(LongProgressBarModel totalProgressModel) {
		this.totalProgressModel = totalProgressModel;
	}
	
	public void setCurrentName(String name) {
		if (hasGUI()) {
			this.nameLabel.setText(name);
		}
	}
	
	/** 
	 * Sets the currentLabel
	 * @param currentLabel
	 */
	public void setCurrentLabel(JLabel currentLabel) {
		this.nameLabel = currentLabel;
	}
	
	/**
	 * Sets the information label used to diplay total time left, speed, number of files left, and size left.
	 * @param label infoLabel
	 */
	public void setInfoLabel(JLabel label) {
		this.infoLabel = label;
	}
	
	/** 
	 * Set hasGUI. If there is GUI it will be updated.
	 * @param b
	 */
	public void setHasGUI(boolean b) {
		this.hasGUI = b;
	}
	
	/**
	 * @return true if there is a GUI, false otherwise
	 */
	public boolean hasGUI() {
		return this.hasGUI;
	}
	
	/**
	 * Updates the loading label
	 */
	public void updateLoadingLabel() {
		if (hasGUI() && this.shouldUpdate()) {
			Logger logger = Logger.getLogger("SuperModel");
			logger.log(Level.FINE, "loading" , copiableList.getCount());
			String loadingText = String.format("Loading %s files", copiableList.getCount());
			this.nameLabel.setText(loadingText);
			this.frame.setTitle(loadingText);
		}
	}

	/**
	 * Sets a common conflict action for all the copiables
	 * @param action
	 */
	public void setConflictActionForAll(ConflictAction action) {
		copiableList.setForAll(action);
	}
	
	/**
	 * Skips the current copiable
	 */
	public void skipCurrent() {
		this.copiableList.getNext().setConflictAction(ConflictAction.SKIP);
	}
}
