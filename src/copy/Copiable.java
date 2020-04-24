package copy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public interface Copiable {
	void copy() throws FileNotFoundException, IOException;
	
	void register();
	
	void setCopied();

	boolean isFile();
	
	boolean isFolder();
	
	boolean destExists();
	
	File getOrigin();
	
	long getSize();
	
	boolean wasCopied();
	
	void setOverwrite();
	
	boolean getOverwriteConfirmation();
	
	void skip();
	
	void renameCoreDest(String newName);
	
	void renameCoreDest(String oldPath, String newPath);
	
	void renameTreeCoreDest(String oldPath, String newPath);  // usded only if it's file
	
	File getAbsoluteDest();
}
