package icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.Icon;

public class ArrowIconLeft implements Icon{
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
	
	public ArrowIconLeft(Color color) {
		this(color,  DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	
	public ArrowIconLeft(Color color, boolean selected) {
		this(color, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public ArrowIconLeft(Color color, int width, int height) {
		this.color = color;
		this.width = width;
		this.height = height;
		initPolygon();
	}
	
	private void initPolygon() {
		poly = new Polygon();
		int halfHeight = height/2;
		poly.addPoint(halfHeight, 0);
		poly.addPoint(width, 0);
		poly.addPoint(width, height);
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
