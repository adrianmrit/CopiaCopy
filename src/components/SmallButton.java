package components;

import java.awt.Insets;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class SmallButton extends DefaultButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2691731139360407943L;
	private final Insets margin = new Insets(0, 0, 0, 0);
	
	public SmallButton(Action a){
		super(a);
		this.setMargin(margin);
	}
	
	public SmallButton(Icon i){
		super(i);
		this.setMargin(margin);
	}
	
	public SmallButton(String text){
		super(text);
		this.setMargin(margin);
	}
	
	public SmallButton(String text, Icon i){
		super(text, i);
		this.setMargin(margin);
	}
}
