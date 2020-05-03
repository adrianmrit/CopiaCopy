package components;

import java.awt.Cursor;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class DefaultButton extends JButton{
	private static final long serialVersionUID = 9027025054375381089L;
	private final Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
	
	public DefaultButton(Action a){
		super(a);
		this.setCursor(cursor);
	}
	
	public DefaultButton(Icon i){
		super(i);
		this.setCursor(cursor);
	}
	
	public DefaultButton(String text){
		super(text);
		this.setCursor(cursor);
	}
	
	public DefaultButton(String text, Icon i){
		super(text, i);
		this.setCursor(cursor);
	}

}
