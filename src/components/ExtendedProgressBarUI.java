package components;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import mdlaf.components.progressbar.MaterialProgressBarUI;

public class ExtendedProgressBarUI extends MaterialProgressBarUI{
	private ExtendedProgressBar progressBar;
	 /**
     * Paints the progress string.
     *
     * @param g Graphics used for drawing.
     * @param x x location of bounding box
     * @param y y location of bounding box
     * @param width width of bounding box
     * @param height height of bounding box
     * @param fillStart start location, in x or y depending on orientation,
     *        of the filled portion of the progress bar.
     * @param amountFull size of the fill region, either width or height
     *        depending upon orientation.
     * @param b Insets of the progress bar.
     */
	
	public ExtendedProgressBarUI(ExtendedProgressBar progressBar) {
		super();
		this.progressBar = progressBar;
	}
	
	public void paintString(Graphics g, int x, int y, int width, int height,
            int fillStart, int amountFull, Insets b) {
		super.paintString(g, x, y, width, height, amountFull, b);
		paintLeftString(g, x, y, width, height, fillStart, amountFull, b);
//		paintString(g, x, y, width, height, amountFull, b);
	}
	
	public void paintString(Graphics g, int x, int y, int width, int height,
            int amountFull, Insets b) {
		super.paintString(g, x, y, width, height, amountFull, b);
		paintLeftString(g, x, y, width, height, 5, amountFull, b);
		paintRightString(g, x, y, width, height, 5, amountFull, b);
	}
	
	
    private void paintLeftString(Graphics g, int x, int y, int width, int height,
                             int fillStart, int amountFull, Insets b) {
        if (!(g instanceof Graphics2D)) {
            return;
        }

        Graphics2D g2 = (Graphics2D)g;
        String leftString = progressBar.getLeftString();
        g2.setFont(progressBar.getFont());
        Point renderLocation = getLeftStringPlacement(g2, leftString,
                                                  x, y, width, height);
        Rectangle oldClip = g2.getClipBounds();

        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            g2.setColor(getSelectionBackground());
            g2.drawString(leftString, renderLocation.x, renderLocation.y);
            g2.setColor(getSelectionForeground());
            g2.clipRect(fillStart, y, amountFull, height);
            g2.drawString(leftString, renderLocation.x, renderLocation.y);
        } else { // VERTICAL
            g2.setColor(getSelectionBackground());
            AffineTransform rotate =
                    AffineTransform.getRotateInstance(Math.PI/2);
            g2.setFont(progressBar.getFont().deriveFont(rotate));
            renderLocation = getLeftStringPlacement(g2, leftString,
                                                  x, y, width, height);
            g2.drawString(leftString, renderLocation.x, renderLocation.y);
            
            g2.setColor(getSelectionForeground());
            g2.clipRect(x, fillStart, width, amountFull);
            
            g2.drawString(leftString, renderLocation.x, renderLocation.y);
        }
        g2.setClip(oldClip);
    }
    
    private void paintRightString(Graphics g, int x, int y, int width, int height,
    							  int fillStart, int amountFull, Insets b) {
		if (!(g instanceof Graphics2D)) {
			return;
		}
			
		Graphics2D g2 = (Graphics2D)g;
		String rightString = progressBar.getRightString();
		g2.setFont(progressBar.getFont());
		Point renderLocation = getRightStringPlacement(g2, rightString,
		                                 x, y, width, height);
		Rectangle oldClip = g2.getClipBounds();
		
		if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
			g2.setColor(getSelectionBackground());
			g2.drawString(rightString, renderLocation.x, renderLocation.y);
			g2.setColor(getSelectionForeground());
			g2.clipRect(fillStart, y, amountFull, height);
			g2.drawString(rightString, renderLocation.x, renderLocation.y);
		} else { // VERTICAL
			g2.setColor(getSelectionBackground());
			AffineTransform rotate =
			   AffineTransform.getRotateInstance(Math.PI/2);
			g2.setFont(progressBar.getFont().deriveFont(rotate));
			renderLocation = getRightStringPlacement(g2, rightString,
			                                 x, y, width, height);
			g2.drawString(rightString, renderLocation.x, renderLocation.y);
			
			g2.setColor(getSelectionForeground());
			g2.clipRect(x, fillStart, width, amountFull);
			
			g2.drawString(rightString, renderLocation.x, renderLocation.y);
		}
			g2.setClip(oldClip);
}


    /**
     * Designate the place where the progress string will be painted.
     * This implementation places it at the center of the progress
     * bar (in both x and y). Override this if you want to right,
     * left, top, or bottom align the progress string or if you need
     * to nudge it around for any reason.
     */
    protected Point getLeftStringPlacement(Graphics g, String progressString,
                                       int x,int y,int width,int height) {
        FontMetrics fontSizer = g.getFontMetrics();
        Rectangle2D stringBound = fontSizer.getStringBounds(progressString, g);

        int stringWidth = (int) stringBound.getWidth();

        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            return new Point(x + 5, // put at beginning
                             y + ((height +
                                 fontSizer.getAscent() -
                                 fontSizer.getLeading() -
                                 fontSizer.getDescent()) / 2));
        } else { // VERTICAL
            return new Point(x + ((width - fontSizer.getAscent() +
                    fontSizer.getLeading() + fontSizer.getDescent()) / 2),
                    y + Math.round(height/2 - stringWidth/2));
        }
    }
    
    protected Point getRightStringPlacement(Graphics g, String progressString,
            int x,int y,int width,int height) {
			FontMetrics fontSizer = g.getFontMetrics();
			Rectangle2D stringBound = fontSizer.getStringBounds(progressString, g);
			
			int stringWidth = (int) stringBound.getWidth();
			
			if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
			return new Point(x + width - stringWidth - 5, // put at end
							 y + ((height +
							      fontSizer.getAscent() -
							      fontSizer.getLeading() -
							      fontSizer.getDescent()) / 2));
			} else { // VERTICAL
				return new Point(x + ((width - fontSizer.getAscent() +
						fontSizer.getLeading() + fontSizer.getDescent()) / 2),
						y + Math.round(height/2 - stringWidth/2));
				}
}
}
