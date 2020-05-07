package enums;

import copy.Copiable;
import copy.CopiableList;

public class ActionUtils {
	/**
	 * Sets a {@link ConflictAction} for all copiables, or for only one.
	 * 
	 * @param copiableList The list that hold all copiables
	 * @param c a copiable
	 * @param action the ConflictAction
	 * @param forAll for all copiables or only one
	 */
	public static void setConflictAction(CopiableList copiableList, Copiable c, ConflictAction action, boolean forAll) {
		if (forAll) {
			copiableList.setForAll(action);
		} else {
			c.setConflictAction(action);
		}
	}
}
