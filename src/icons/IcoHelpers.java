package icons;

import java.io.File;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

public class IcoHelpers {
	public static Icon loadFileIco(File f) {
		Icon icon = FileSystemView.getFileSystemView().getSystemIcon(f);
		return icon;
	}
}
