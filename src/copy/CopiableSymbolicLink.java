package copy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import enums.ConflictAction;
import enums.CopyMode;
import exceptions.CopyExceptionFactory;
import models.SuperModel;

public class CopiableSymbolicLink extends CopiableAbstract{

	public CopiableSymbolicLink(Path origin, Path rootOrigin, Path rootDest, SuperModel SM, Copiable parent, CopyMode mode) {
		super(origin, rootOrigin, rootDest, SM, parent, mode);
	}
	
	/**
	 * Copies the symbolic link
	 */
	public void copy() {
		if (!this.wasCopied() && this.getConflictAction() != ConflictAction.SKIP) {
			try {
				Files.copy(this.getOrigin(), this.getDest(),
						LinkOption.NOFOLLOW_LINKS,
						StandardCopyOption.COPY_ATTRIBUTES,
						StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				this.setError(CopyExceptionFactory.write());
				e.printStackTrace();
			}
			this.setCopied();
		} else {
			this.SM.copiableList.movePointer();
		}
	}

	@Override
	public long getSize() {
		return 0;
	}

	@Override
	public long getSizeRec() {
		return 0;
	}

	@Override
	public void renameCoreDest(String newName) {
		Path parent = this.getCoreDest().getParent();
		Path newPath = Paths.get(parent.toString(), newName);

		this.setCoreDestPath(newPath);
		
	}

	@Override
	public void renameTreeCoreDest(String oldPath, String newPath) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setConflictAction(ConflictAction action) {
		if (action != ConflictAction.MERGE) { // symbolic links are not supposed to be merged
			super.setConflictAction(action);
		}
	}

}
