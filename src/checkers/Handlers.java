package checkers;

import copy.Copiable;
import copy.SuperModel;

public class Handlers implements Checker{
	private Checker[] checkers;
	
	/**
	 * Runs different handlers. 
	 * <br>
	 * <br>
	 * <b>Handlers</b> (in order) <b>:</b>
	 * <br>
	 * {@link DestEqualsOrigin} <br>
	 * {@link CopyIntoItself} <br>
	 * {@link Exists}
	 * @param SM SuperModel that hold some necessary objects.
	 */
	public Handlers(SuperModel SM) {
		this.checkers = new Checker[] {
				new DestEqualsOrigin(),
				new CopyIntoItself(SM),
				new Exists(SM)
		};
	}
	/**
	 * Runs a set of handlers
	 */
	@Override
	public boolean handle(Copiable c) {
		boolean handleNext = true;
		for (Checker checker : checkers) {
			handleNext = checker.handle(c);
			if (!handleNext) break;  // if a checker returns false then stop there. See documentation for handlers
		}
		return handleNext;
	}

}
