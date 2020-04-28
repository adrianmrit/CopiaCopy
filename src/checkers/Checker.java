package checkers;

import copy.Copiable;

/**
 * Interface that represent a class that realizes an action when a
 * {@link Copiable} object is passed to {@link #handle}.
 */
public interface Checker {
	
	/**
	 * A function that does something with a {@link Copiable} object.
	 * Returns true if next handlers should be handled, false otherwise.
	 * @param c {@link Copiable} object to be handled
	 * @return true if next handlers should be handled, false otherwise.
	 */
	public boolean handle(Copiable c);

}
