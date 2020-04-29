package gui;

import java.io.File;

import javax.swing.JFrame;

public final class ExistsDialogBuilder {
	public static final String CANCEL  = "cancel";
	public static final String SKIP  = "skip";
	public static final String REPLACE  = "replace";
	public static final String RENAME  = "rename";
	public static final String MERGE  = "merge";
	
	private ExistsDialogBuilder() {}
	
	private static String toHTMLTitle(String title) {
		return "<HTML><h1><b>" + title + "</b></h1></HTML>";
	}
	
	private static String toHTMLParagraph(String message) {
		return "<HTML><p>" + message + "</p></HTML>";
	}
	
	private static String getSymbolicLinkExistsTitle(File dest) {
		return toHTMLTitle("Replace symbolic link \"" + dest.getName() + "\"?");
	}
	
	private static String getFileExistsTitle(File dest) {
		return toHTMLTitle("Replace file \"" + dest.getName() + "\"?");
	}
	
	private static String getFolderExistsTitle(File dest) {
		return toHTMLTitle("Replace folder \"" + dest.getName() + "\"?");
	}
	
	private static String getSymbolicLinkExistsMessage(File dest) {
		String parentFolderName = dest.getParentFile().getName();
		String message = "Anoter symbolic link with the same name already exists in \"" + parentFolderName +  "\".";
		return toHTMLParagraph(message);
	}
	
	private static String getFileExistsMessage(File dest) {
		String parentFolderName = dest.getParentFile().getName();
		String message = "Anoter file with the same name already exists in \"" + parentFolderName +  "\"."
				+ "Replacing will overwrite its content";
		return toHTMLParagraph(message);
	}
	
	private static String getFolderExistsMessage(File dest) {
		String parentFolderName = dest.getParentFile().getName();
		String message = "Anoter folder with the same name already exists in \"" + parentFolderName +  "\"."
				+ "Mergin will ask for confirmation if further files exist";
		return toHTMLParagraph(message);
	}
	
	public static void setCommons(ExistsDialog dialog) {
		dialog.setCancelButton("Cancel", CANCEL);
		dialog.setSkipButton("Skip", SKIP);
		dialog.setRenameButton("Rename", RENAME);
	}
	
	public static ExistsDialog getFileExistsDialog(JFrame parent, File origin, File dest) {
		ExistsDialog dialog = new ExistsDialog(parent, origin, dest);
		
		String title = getFileExistsTitle(dest);
		String message = getFileExistsMessage(dest);
		
		dialog.setWindowsTitle("Replace file");
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setReplaceWithBox(origin);
		
		dialog.setOriginalBox(dest);
		
		setCommons(dialog);
		
		dialog.setMRButton("Replace", REPLACE);

		return dialog;
	}
	
	public static ExistsDialog getFolderExistsDialog(JFrame parent, File origin, File dest) {
		ExistsDialog dialog = new ExistsDialog(parent, origin, dest);
		
		String title = getFolderExistsTitle(dest);
		String message = getFolderExistsMessage(dest);
		
		dialog.setWindowsTitle("Merge folders");
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setReplaceWithBox(origin);
		
		dialog.setOriginalBox(dest);
		
		setCommons(dialog);
		
		dialog.setMRButton("Merge", MERGE);

		return dialog;
	}
	
	public static ExistsDialog getSymbolicLinkExistsDialog(JFrame parent, File origin, File dest) {
		ExistsDialog dialog = new ExistsDialog(parent, origin, dest);
		
		String title = getSymbolicLinkExistsTitle(dest);
		String message = getSymbolicLinkExistsMessage(dest);
		
		dialog.setWindowsTitle("Replace");
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setReplaceWithBox(origin);
		
		dialog.setOriginalBox(dest);
		
		setCommons(dialog);
		
		dialog.setMRButton("Replace", REPLACE);

		return dialog;
	}

}
