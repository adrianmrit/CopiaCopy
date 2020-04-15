package listeners;

import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gui.LongProgressBarModel;

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
				System.out.println ("Changed: " + progressBarModel.getValue());
			}
		}
	}
}
