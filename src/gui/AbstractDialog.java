package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public abstract class AbstractDialog {
	private JFrame parent;
	private String actionPerformed;
	private JDialog dialog;
	
	private final ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent actionEvent) {
			actionPerformed = actionEvent.getActionCommand();
				dialog.setVisible(false);
			}
		};

	public AbstractDialog(JFrame parent){
		this.parent = parent;
	}
	
	public JFrame getParent() {
		return this.parent;
	}

	public void registerButton(JButton button, String command) {
		button.addActionListener(actionListener);
		button.setActionCommand(command);
	}
	
	public String getAction() {
		return this.actionPerformed;
	}
	
	public void setJDialog(JDialog dialog) {
		this.dialog = dialog;
	}
	
	public JDialog getJDialog() {
		return this.dialog;
	}
	
	public void showDialog() {
		this.dialog.setVisible(true);
	}
	
	public void hideDialog() {
		this.dialog.setVisible(false);
	}
	
	public void setSize(int width, int height) {
		this.dialog.setSize(width, height);
	}

}