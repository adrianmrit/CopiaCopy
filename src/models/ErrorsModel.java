package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import copy.Copiable;
import enums.ConflictAction;
import languages.LangBundle;

public class ErrorsModel extends AbstractTableModel{
	private static final long serialVersionUID = -4626313003036286748L;
	
	private List<Copiable> withErrors = new ArrayList<>();
	
	public void insert(Copiable c) {
		withErrors.add(c);
		this.fireTableRowsInserted(withErrors.size(), withErrors.size());
	}
	
	public void remove(Copiable c) {
		int index = withErrors.indexOf(c);
		if (index != -1) {
			remove(index);	
		}
	}
	
	public void remove(int index) {
		withErrors.remove(index);
		this.fireTableRowsDeleted(index, index);
	}
	
	public void skip(int index) {
		withErrors.get(index).setConflictAction(ConflictAction.SKIP);
//		remove(index); // Do not remove directly. Copiables should be removed from queue when skip
	}
	
//	public void skip(int[] indexes) {
//		// Delete from higher indexes to lower indexes so there is no problem while deleting.
//		Arrays.parallelSort(indexes);
//		ArrayUtils.reverse(indexes);
//		
//		for (int i:indexes) {
//			skip(i);
//		}
////		ListUtils.removeAll(arg0, arg1)
//	}

	@Override
	public int getRowCount() {
		return withErrors.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}
	
	@Override
	public String getColumnName(int index) {
		switch (index) {
			case 0:
				return LangBundle.CURRENT.getString("name");
			case 1:
				return LangBundle.CURRENT.getString("origin");
			default:
				return LangBundle.CURRENT.getString("error");
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Copiable c = withErrors.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return c.getOrigin().getFileName();
			case 1:
				return c.getOrigin().getParent();
			default:
				return c.getError().getMessage();
		}
	}
}
