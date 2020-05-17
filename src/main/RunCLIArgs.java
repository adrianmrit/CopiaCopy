package main;

import java.nio.file.Path;
import java.nio.file.Paths;

import copy.Copiable;
import copy.Copy;
import enums.CopyMode;
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
		
		CopyMode mode;
		switch (args[2]) {
			case "c":
			case "C":
				mode = CopyMode.COPY;
				break;
			case "m":
			case "M":
				mode = CopyMode.MOVE;
				break;
			default:
				mode = CopyMode.COPY;
				break;
		}
		
		CopyGUI.setDebug(true);
		CopyGUI copy = new CopyGUI(src.toString(), dest.toString(), mode);
		Copy.setDebug(false);
		copy.run();
	}
}
