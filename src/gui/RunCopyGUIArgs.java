package gui;

import java.nio.file.Path;
import java.nio.file.Paths;

import copy.Copiable;
import copy.Copy;
import testFiles.FileFactory;

public class RunCopyGUIArgs {
	public static void main(String args[]) {		
//		Path src = FileFactory.TEST_FOLDER;
//		Path dest = FileFactory.TEST_DEST_FOLDER_PARENT; // must exist

		Path src = Paths.get("/home/adrianmrit/Downloads/");
		Path dest = Paths.get("/home/adrianmrit/Desktop/testF"); // must exist
		
		CopyGUI.setDebug(true);
		CopyGUI copy = new CopyGUI(src.toString(), dest.toString(), Copiable.COPY_MODE);
		Copy.setDebug(true);
		copy.run();
	}
}
