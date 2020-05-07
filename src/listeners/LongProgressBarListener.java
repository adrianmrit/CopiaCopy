package listeners;

import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import models.LongProgressBarModel;

public class LongProgressBarListener implements ChangeListener{
	private JProgressBar progressBar;
	
	public LongProgressBarListener(JProgressBar progressBar) {
		super();
		this.progressBar = progressBar;
	}
	
	public void stateChanged(ChangeEvent changeEvent) {
		Object source = changeEvent.getSource();
		if (source instanceof LongProgressBarModel) {
			LongProgressBarModel progressBarModel = (LongProgressBarModel)source;
			if (!progressBarModel.getValueIsAdjusting()) {
				progressBar.setValue(progressBarModel.getValue());
				progressBar.setIndeterminate(progressBarModel.isIndeterminate());
				progressBar.setStringPainted(progressBarModel.isStringPainted());
			}
		}
	}
}
