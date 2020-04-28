package checkers;

import org.apache.commons.io.FilenameUtils;

import copy.Copiable;
import copy.NameFactory;

public class DestEqualsOrigin implements Checker{
	
	/**
	 * Destination equals origin handler. 
	 */
	public DestEqualsOrigin() {}
	
	/**
	 * Handle situations where destination equals origin.
	 * A copy of the file will be made.
	 */
	public boolean handle(Copiable c) {
		if (c.getDest().equals(c.getOrigin())) {
			String newName = NameFactory.getUnique(c.getOrigin().toString());
			newName = FilenameUtils.getName(newName);
			
			c.renameCoreDest(newName);
			return false;
		}
		
		return true;
	}
}
