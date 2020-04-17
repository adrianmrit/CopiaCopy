package components;

import java.awt.Cursor;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class BigButton extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5396225219492398843L;
	private final Insets margin = new Insets(5, 5, 5, 5);
	private final Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
	
	public BigButton(Action a){
		super(a);
		this.setMargin(margin);
	}
	
	public BigButton(Icon i){
		super(i);
		this.setMargin(margin);
	}
	
	public BigButton(String text){
		super(text);
		this.setMargin(margin);
	}
	
	public BigButton(String text, Icon i){
		super(text, i);
		this.setMargin(margin);
	}
}
