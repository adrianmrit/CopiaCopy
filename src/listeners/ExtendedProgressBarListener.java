package listeners;

import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import components.ExtendedProgressBar;
import gui.ExtendedProgressBarModel;
import gui.LongProgressBarModel;

public class ExtendedProgressBarListener implements ChangeListener{
	private ExtendedProgressBar progressBar;
	
	public ExtendedProgressBarListener(ExtendedProgressBar progressBar) {
		super();
		this.progressBar = progressBar;
	}
	
	public void stateChanged(ChangeEvent changeEvent) {
		Object source = changeEvent.getSource();
		if (source instanceof ExtendedProgressBarModel) {
			ExtendedProgressBarModel progressBarModel = (ExtendedProgressBarModel)source;
			progressBar.setLeftString(progressBarModel.getSizeLeft());
			progressBar.setRightString(progressBarModel.getTimeLeft());
			progressBar.setValue(progressBarModel.getValue());
		}
	}
}
