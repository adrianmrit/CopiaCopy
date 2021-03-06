package icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.Icon;

public class ArrowIconRight implements Icon{
	private Color color;
	private int width;
	private int height;
	private Polygon poly;
	private static final int DEFAULT_WIDTH = 10;
	private static final int DEFAULT_HEIGHT = 10;
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	
	public ArrowIconRight(Color color) {
		this(color,  DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	
	public ArrowIconRight(Color color, boolean selected) {
		this(color, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public ArrowIconRight(Color color, int width, int height) {
		this.color = color;
		this.width = width;
		this.height = height;
		initPolygon();
	}
	
	private void initPolygon() {
		poly = new Polygon();
		int halfHeight = height/2;
		poly.addPoint(width, halfHeight);
		poly.addPoint(0, 0);
		poly.addPoint(0, height);
	}
	
	
	public int getIconHeight() {
		return height;
	}
	public int getIconWidth() {
		return width;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(color);
		g.translate(x, y);
		g.fillPolygon(poly);
		g.translate(-x, -y);
	}
}
