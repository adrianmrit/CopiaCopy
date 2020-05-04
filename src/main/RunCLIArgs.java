package main;

import java.nio.file.Path;
import java.nio.file.Paths;

import copy.Copiable;
import copy.Copy;
import gui.CopyGUI;

/**
 * Runs the copy from the command line.
 * @author adrianmrit
 *
 */
public class RunCLIArgs {
	public static void main(String args[]) {
		Path src = Paths.get(args[0]).toAbsolutePath();
		Path dest = Paths.get(args[1]).toAbsolutePath(); // must exist
		
		int mode;
		switch (args[2]) {
			case "c":
				mode = Copiable.COPY_MODE;
				break;
			case "C":
				mode = Copiable.COPY_MODE;
				break;
			case "m":
				mode = Copiable.CUT_MODE;
				break;
			case "M":
				mode = Copiable.CUT_MODE;
				break;
			default:
				mode = Copiable.COPY_MODE;
				break;
		}
		
		CopyGUI.setDebug(true);
		CopyGUI copy = new CopyGUI(src.toString(), dest.toString(), mode);
		Copy.setDebug(false);
		copy.run();
	}
}
