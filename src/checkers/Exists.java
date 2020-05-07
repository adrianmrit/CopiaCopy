package checkers;

import java.nio.file.Files;

import copy.Copiable;
import copy.SuperModel;
import enums.ActionUtils;
import enums.ConflictAction;
import gui.ExistsDialog;
import gui.ExistsDialogBuilder;

public class Exists implements Checker{
	private SuperModel SM;
	
	/**
	 * File exists handler.
	 * @param SM
	 */
	public Exists(SuperModel SM) {
		this.SM = SM;
	}
	
	/**
	 * Check if there is any file in the copy that exists in the origin.
	 * If it does it will ask what to do with it.
	 */
	@Override
	public boolean handle(Copiable c) {
		if (c.destExists() && c.getConflictAction() == ConflictAction.DEFAULT) {
			if (c.isSymbolicLink()) {
				handleSymbolicLinkExistDialog(c);
			} else if (c.isFile()) {
				handleFileExistDialog(c);
			} else {
				handleFolderExistDialog(c);
			}
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * Shows a dialog if the file already exists in the destination.
	 * @param origin Origin path
	 * @param dest Destination path
	 */
	private void handleFileExistDialog(Copiable c) {
		if (SM.hasGUI()) {
			ExistsDialog dialog = ExistsDialogBuilder.getFileExistsDialog(SM.frame, c.getOrigin().toFile(), c.getDest().toFile());
			dialog.show();
			String action = dialog.getAction();
			switch (action) {
				case ExistsDialogBuilder.CANCEL:
					System.exit(0);  // exit copy// TODO: handle file exists
					break;
				
				case ExistsDialogBuilder.SKIP:
					ActionUtils.setConflictAction(SM.copiableList, c, ConflictAction.SKIP, dialog.isForAll());
					SM.totalProgressModel.setLongMaximum(SM.copiableList.getTotalSize());
					break;
				
				case ExistsDialogBuilder.RENAME:
					c.renameCoreDest(dialog.getInputValue());
					break;
				
				case ExistsDialogBuilder.REPLACE:
					ActionUtils.setConflictAction(SM.copiableList, c, ConflictAction.REPLACE, dialog.isForAll());
					break;
			}
		}
		else {
			c.setConflictAction(ConflictAction.REPLACE);
		}
	}
	
	/**
	 * Shows a dialog if the folder already exists in the destination.
	 * @param origin Origin path
	 * @param dest Destination path
	 */
	private void handleFolderExistDialog(Copiable c) {
		if (SM.hasGUI()) {
			ExistsDialog dialog = ExistsDialogBuilder.getFolderExistsDialog(SM.frame, c.getOrigin().toFile(), c.getDest().toFile());
			dialog.show();
			String action = dialog.getAction();
			switch (action) {
				case ExistsDialogBuilder.CANCEL:
					System.exit(0);  // exit copy// TODO: handle file exists
					break;
				
				case ExistsDialogBuilder.SKIP:
					ActionUtils.setConflictAction(SM.copiableList, c, ConflictAction.SKIP, dialog.isForAll());
					SM.totalProgressModel.setLongMaximum(SM.copiableList.getTotalSize());
					break;
				
				case ExistsDialogBuilder.RENAME:
					c.renameCoreDest(dialog.getInputValue());
					break;
				
				case ExistsDialogBuilder.MERGE:
					ActionUtils.setConflictAction(SM.copiableList, c, ConflictAction.MERGE, dialog.isForAll());
					break;
			}
		} else {
			c.setConflictAction(ConflictAction.MERGE);
		}
	}
	
	/**
	 * Shows a dialog if the folder already exists in the destination.
	 * @param origin Origin path
	 * @param dest Destination path
	 */
	private void handleSymbolicLinkExistDialog(Copiable c) {
		if (SM.hasGUI()) {
			ExistsDialog dialog = ExistsDialogBuilder.getSymbolicLinkExistsDialog(SM.frame, c.getOrigin().toFile(), c.getDest().toFile());
			dialog.show();
			String action = dialog.getAction();
			switch (action) {
				case ExistsDialogBuilder.CANCEL:
					System.exit(0);  // exit copy// TODO: handle file exists
					break;
				
				case ExistsDialogBuilder.SKIP:
					ActionUtils.setConflictAction(SM.copiableList, c, ConflictAction.SKIP, dialog.isForAll());
//					c.skip();
					SM.totalProgressModel.setLongMaximum(SM.copiableList.getTotalSize());
					break;
				
				case ExistsDialogBuilder.RENAME:
					c.renameCoreDest(dialog.getInputValue());
					break;
				
				case ExistsDialogBuilder.REPLACE:
					ActionUtils.setConflictAction(SM.copiableList, c, ConflictAction.REPLACE, dialog.isForAll());
					break;
			}
		} else {
			c.setConflictAction(ConflictAction.REPLACE);
		}
	}

}
