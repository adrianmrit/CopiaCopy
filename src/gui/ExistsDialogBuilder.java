package gui;

import java.io.File;

import javax.swing.JFrame;

import languages.LangBundle;

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
		return toHTMLTitle(LangBundle.CURRENT.format("replaceShortcutTitleFormat", dest.getName()));
	}
	
	private static String getFileExistsTitle(File dest) {
		return toHTMLTitle(LangBundle.CURRENT.format("replaceFileTitleFormat", dest.getName()));
	}
	
	private static String getFolderExistsTitle(File dest) {
		return toHTMLTitle(LangBundle.CURRENT.format("mergeFolderTitleFormat", dest.getName()));
	}
	
	private static String getSymbolicLinkExistsMessage(File dest) {
		String parentFolderName = dest.getParentFile().getName();
		String message = LangBundle.CURRENT.format("existsShortcutFormat", parentFolderName);
		return toHTMLParagraph(message);
	}
	
	private static String getFileExistsMessage(File dest) {
		String parentFolderName = dest.getParentFile().getName();
		String message = LangBundle.CURRENT.format("existsFileFormat", parentFolderName);
		return toHTMLParagraph(message);
	}
	
	private static String getFolderExistsMessage(File dest) {
		String parentFolderName = dest.getParentFile().getName();
		String message = LangBundle.CURRENT.format("existsFolderFormat", parentFolderName);
		return toHTMLParagraph(message);
	}
	
	public static void setCommons(ExistsDialog dialog) {
		dialog.setCancelButton(LangBundle.CURRENT.getString("cancel"), CANCEL);
		dialog.setSkipButton(LangBundle.CURRENT.getString("skip"), SKIP);
		dialog.setRenameButton(LangBundle.CURRENT.getString("rename"), RENAME);
	}
	
	public static ExistsDialog getFileExistsDialog(JFrame parent, File origin, File dest) {
		ExistsDialog dialog = new ExistsDialog(parent, origin, dest);
		
		String title = getFileExistsTitle(dest);
		String message = getFileExistsMessage(dest);
		
		dialog.setWindowsTitle(LangBundle.CURRENT.getString("existsFileWindowsTitle"));
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setReplaceWithBox(origin);
		
		dialog.setOriginalBox(dest);
		
		setCommons(dialog);
		
		dialog.setMRButton(LangBundle.CURRENT.getString("replace"), REPLACE);

		return dialog;
	}
	
	public static ExistsDialog getFolderExistsDialog(JFrame parent, File origin, File dest) {
		ExistsDialog dialog = new ExistsDialog(parent, origin, dest);
		
		String title = getFolderExistsTitle(dest);
		String message = getFolderExistsMessage(dest);
		
		dialog.setWindowsTitle(LangBundle.CURRENT.getString("existsFolderWindowsTitle"));
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setReplaceWithBox(origin);
		
		dialog.setOriginalBox(dest);
		
		setCommons(dialog);
		
		dialog.setMRButton(LangBundle.CURRENT.getString("merge"), MERGE);

		return dialog;
	}
	
	public static ExistsDialog getSymbolicLinkExistsDialog(JFrame parent, File origin, File dest) {
		ExistsDialog dialog = new ExistsDialog(parent, origin, dest);
		
		String title = getSymbolicLinkExistsTitle(dest);
		String message = getSymbolicLinkExistsMessage(dest);
		
		dialog.setWindowsTitle(LangBundle.CURRENT.getString("existsShortcutWindowsTitle"));
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setReplaceWithBox(origin);
		
		dialog.setOriginalBox(dest);
		
		setCommons(dialog);
		
		dialog.setMRButton(LangBundle.CURRENT.getString("replace"), REPLACE);

		return dialog;
	}

}
