package checkers;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import copy.Copiable;
import enums.ConflictAction;
import exceptions.CopyException;
import exceptions.CopyExceptionFactory;
import languages.LangBundle;
import models.SuperModel;

public class DifferentTypeExists implements Checker{
	private SuperModel SM;
	public DifferentTypeExists(SuperModel SM){
		this.SM = SM;
	}

	/**
	 * Handle situation where the destination exists but it is a different type
	 * An error will be thrown and the item will be skipped in the copy.
	 */
	public boolean handle(Copiable c) {
		Path orig = c.getOrigin();
		Path dest = c.getDest();

		if (Files.isDirectory(dest, LinkOption.NOFOLLOW_LINKS) && !Files.isDirectory(orig, LinkOption.NOFOLLOW_LINKS)) {

			JOptionPane.showMessageDialog(SM.frame,
					LangBundle.CURRENT.format("ExistButItsFolderError", orig),
					LangBundle.CURRENT.getString("CopyError"),
					JOptionPane.ERROR_MESSAGE);
			
			c.setError(CopyExceptionFactory.nameConflict());
			c.setConflictAction(ConflictAction.SKIP);

			return false;
		}

		else if (Files.isRegularFile(dest, LinkOption.NOFOLLOW_LINKS) && !Files.isRegularFile(orig, LinkOption.NOFOLLOW_LINKS)) {

			JOptionPane.showMessageDialog(SM.frame,
					LangBundle.CURRENT.format("ExistButItsFileError", orig),
					LangBundle.CURRENT.getString("CopyError"),
					JOptionPane.ERROR_MESSAGE);
			
			c.setError(CopyExceptionFactory.nameConflict());
			c.setConflictAction(ConflictAction.SKIP);

			return false;
		}

		else if (Files.isSymbolicLink(dest) && !Files.isSymbolicLink(orig)) {
			JOptionPane.showMessageDialog(SM.frame,
					LangBundle.CURRENT.format("ExistButItsShortcutError", orig),
					LangBundle.CURRENT.getString("CopyError"),
					JOptionPane.ERROR_MESSAGE);
			
			c.setError(CopyExceptionFactory.nameConflict());
			c.setConflictAction(ConflictAction.SKIP);

			return false;
		}

		return true;
	}

}
