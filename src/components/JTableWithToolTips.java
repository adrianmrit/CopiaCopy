package components;

import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.TableModel;


public class JTableWithToolTips extends JTable{
	private static final long serialVersionUID = -7298930587998020280L;

	public JTableWithToolTips(TableModel copyQueueModel) {
		super(copyQueueModel);
	}

	public String getToolTipText(MouseEvent e) {
		String tip = null;
		java.awt.Point p = e.getPoint();
		int rowIndex = rowAtPoint(p);
		int colIndex = columnAtPoint(p);
		try {
			tip = getValueAt(rowIndex, colIndex).toString();
		} catch (RuntimeException exception) {
			// catch null pointer exception
		}
		
		return tip;
	}

}
