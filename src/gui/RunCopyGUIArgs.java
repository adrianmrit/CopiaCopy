package gui;

import java.nio.file.Path;
import java.nio.file.Paths;

import copy.Copy;

public class RunCopyGUIArgs {
	public static void main(String args[]) {		
		Path src = Paths.get("Sample/").toAbsolutePath();
		Path dest = Paths.get("TestDest/").toAbsolutePath();
		
		CopyGUI.setDebug(true);
		Copy.setDebug(true);
		CopyGUI.main(new String[] {src.toString(), dest.toString()});
	}
}
