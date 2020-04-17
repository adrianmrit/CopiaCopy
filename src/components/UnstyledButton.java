package components;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class UnstyledButton extends DefaultButton{
	public UnstyledButton(Action a){
		super(a);
		this.setContentAreaFilled(false);
	}
	
	public UnstyledButton(Icon i){
		super(i);
		this.setContentAreaFilled(false);
	}
	
	public UnstyledButton(String text){
		super(text);
		this.setContentAreaFilled(false);
	}
	
	public UnstyledButton(String text, Icon i){
		super(text, i);
		this.setContentAreaFilled(false);
	}
}
