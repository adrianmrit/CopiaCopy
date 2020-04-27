package gui;

import java.nio.file.Path;
import java.nio.file.Paths;

import copy.Copy;

public class RunCopyGUIArgs {
	public static void main(String args[]) {		
		Path src = Paths.get("Sample/").toAbsolutePath();
		Path dest = Paths.get("Sample/").toAbsolutePath();
		
		CopyGUI.setDebug(true);
		CopyGUI copy = new CopyGUI(src.toString(), dest.toString());
		Copy.setDebug(true);
		copy.run();
	}
}
