package checkers;

import java.nio.file.Path;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import copy.Copiable;
import copy.SuperModel;

public class CopyIntoItself implements Checker{
	private SuperModel SM;
	public CopyIntoItself(SuperModel SM){
		this.SM = SM;
	}
	
	/**
	 * Handle situation where there is an attempt to copy a folder into itself.
	 * An error will be thrown and the folder will be skipped in the copy.
	 */
	public boolean handle(Copiable c) {
		if (c.isFolder()) {
			Path orig = c.getOrigin().toPath();
			Path dest = c.getDest().toPath();
			
			if (dest.startsWith(orig)) { // checks if dest is sub-folder of orig
				
				JOptionPane optionPane = new JOptionPane("You can't copy a folder into itself",
						JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
						
				JDialog dialog = optionPane.createDialog(SM.frame, "Copy error");
						dialog.setVisible(true);

				c.skip(); // skips the tree
				
				return false;
			}
		}
		
		return true;
	}

}
