package gui;

import java.nio.file.Path;
import java.nio.file.Paths;

import copy.Copiable;
import copy.Copy;
import testFiles.TestFileFactory;

public class RunCopyGUIArgs {
	public static void main(String args[]) {		
		Path src = TestFileFactory.TEST_FOLDER;
		Path dest = TestFileFactory.TEST_DEST_FOLDER_PARENT;

		CopyGUI.setDebug(true);
		CopyGUI copy = new CopyGUI(src.toString(), dest.toString(), Copiable.CUT_MODE);
		Copy.setDebug(true);
		copy.run();
	}
}
