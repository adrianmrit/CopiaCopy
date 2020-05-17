package windowsTests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Locale;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import copy.Copiable;
import copy.CopiableFile;
import dialogs.FileNotFoundErrorDialog;
import dialogs.FolderAccessErrorDialog;
import dialogs.ReadPermissionErrorDialog;
import dialogs.WriteErrorDialog;
import dialogs.WritePermissionErrorDialog;
import enums.CopyMode;
import gui.ExistsDialog;
import gui.ExistsDialogBuilder;
import languages.LangBundle;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialTheme;
import models.SuperModel;
import testFiles.FileFactory;
import themes.JMarsDarkCustom;
import utils.SystemProps;

class TestDialogs {
	private static JFrame frame;
	@BeforeAll
	static void setUp() {
		LangBundle.load(Locale.forLanguageTag("es-ES"));
		SystemProps.setBooleanProp("debug", true);
		frame = new JFrame();
		try {
			JDialog.setDefaultLookAndFeelDecorated(true);
			MaterialTheme theme = new JMarsDarkCustom();
			UIManager.setLookAndFeel (new MaterialLookAndFeel (theme));
		}
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace ();
		}
	}
	
	@BeforeEach
	@AfterEach
	void setupTestDir() throws IOException {
		FileFactory.createTestFiles();
		
		try {
			FileUtils.deleteDirectory(FileFactory.TEST_DEST_FOLDER_PARENT.toFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileFactory.TEST_DEST_FOLDER_PARENT.toFile().mkdir();
	}
	
	@Test
	void testFileExistsDialog() {
		Copiable c = new CopiableFile(FileFactory.DEST_FILE_1, FileFactory.DEST_FILE_1.getParent(), FileFactory.TEST_DEST_FOLDER, null, null, CopyMode.COPY);
		ExistsDialog dialog = ExistsDialogBuilder.getFileExistsDialog(frame, c.getOrigin().toFile(), c.getDest().toFile());
		dialog.show();
	}
	
	@Test
	void testShortcutExistsDialog() {
		Copiable c = new CopiableFile(FileFactory.DEST_FILE_1, FileFactory.DEST_FILE_1.getParent(), FileFactory.TEST_DEST_FOLDER, null, null, CopyMode.COPY);
		ExistsDialog dialog = ExistsDialogBuilder.getSymbolicLinkExistsDialog(frame, c.getOrigin().toFile(), c.getDest().toFile());
		dialog.show();
	}
	
	@Test
	void testFolderExistsDialog() {
		Copiable c = new CopiableFile(FileFactory.DEST_SUB_FOLDER, FileFactory.DEST_SUB_FOLDER.getParent(), FileFactory.TEST_DEST_FOLDER, null, null, CopyMode.COPY);
		ExistsDialog dialog = ExistsDialogBuilder.getFolderExistsDialog(frame, c.getOrigin().toFile(), c.getDest().toFile());
		dialog.show();
	}
	
	@Test
	void testFileNotFoundErrorDialog() {
		new FileNotFoundErrorDialog().showDialog(frame, FileFactory.DEST_FILE_1);
	}
	
	@Test
	void testFolderAccessErrorDialog() {
		new FolderAccessErrorDialog().showDialog(frame, FileFactory.DEST_SUB_FOLDER);
	}
	
	@Test
	void testReadPermissionErrorDialog() {
		new ReadPermissionErrorDialog().showDialog(frame, FileFactory.DEST_FILE_1);
	}
	
	@Test
	void testWriteErrorDialog() {
		new WriteErrorDialog().showDialog(frame, FileFactory.DEST_FILE_1);
	}
	
	@Test
	void testWritePermissionErrorDialog() {
		new WritePermissionErrorDialog().showDialog(frame, FileFactory.DEST_FILE_1);
	}

}
