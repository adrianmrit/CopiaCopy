package copy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import dialogs.FileNotFoundErrorDialog;
import dialogs.FolderAccessErrorDialog;
import dialogs.ReadPermissionErrorDialog;
import dialogs.WriteErrorDialog;
import dialogs.WritePermissionErrorDialog;
import enums.ConflictAction;
import exceptions.CopyException;
import exceptions.CopyExceptionFactory;
import models.SuperModel;
import utils.NameFactory;

/**
 * A file writer. 
 * 
 * @author adrianmrit
 *
 */
public class FileWriter extends SwingWorker<Boolean, Object>{
	CopiableFile copiableFile;
	SuperModel SM;
//	FileLock outputLock;

	/**
	 * Methods for handling the copy of a file.
	 * @param copiable
	 * @param SM
	 */
	FileWriter(CopiableFile copiable, SuperModel SM){
		this.copiableFile = copiable;
		this.SM = SM;
	}

	/**
	 * Gets a {@link FileChannel} in read mode. If an exception occurs an error
	 * dialog will be shown and null will be returned.
	 * @param position, position at which the fileChannel will be opened.
	 * @return a FileChannel in read mode, or null if an exception occurs
	 */
	private FileChannel loadInputChannel (long position) throws CopyException{
		try{
			FileChannel input = FileChannel.open(copiableFile.getOrigin(), StandardOpenOption.READ);
//			try {
//				input.position(position);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			return input;
		} catch (SecurityException e) {
			int value = new ReadPermissionErrorDialog().showDialog(SM.frame, copiableFile.getOrigin());
			if (value == JOptionPane.NO_OPTION) {
				throw CopyExceptionFactory.permission();
//				copiableFile.setConflictAction(ConflictAction.SKIP);
			}
		} catch (IOException e) {
			int value = new FileNotFoundErrorDialog().showDialog(SM.frame, copiableFile.getOrigin());
			if (value == JOptionPane.NO_OPTION) {
				throw CopyExceptionFactory.notFound();
//				copiableFile.setConflictAction(ConflictAction.SKIP);
			}
		}
		return null;
	}

	/**
	 * Loads a {@link FileChannel} in write and append mode. If the file doesn't exist it will be created.
	 * If an exception occurs an error
	 * dialog will be shown and null will be returned.
	 * @return a FileChannel in read mode, or null if an exception occurs
	 * @throws CopyException 
	 */
	private FileChannel loadOutputChannel(long position) throws CopyException{
		try{
			FileChannel output = FileChannel.open(copiableFile.getTemp(),
					StandardOpenOption.WRITE,
					StandardOpenOption.APPEND,
					StandardOpenOption.CREATE);
//			outputLock = output.tryLock(0L, Long.MAX_VALUE, true);
			output.position(output.size()); // makes sure the file continues the copy at the end of the file
			
			// if a copy has to reload, maybe some bytes where not written. Thus we should update the progress bar.
			SM.restCopiedBytes(copiableFile.getCopyPosition() - output.position());
				
			return output;
		} catch (SecurityException e) {
			int value = new WritePermissionErrorDialog().showDialog(SM.frame, copiableFile.getOrigin());
			if (value == JOptionPane.NO_OPTION) {
				throw CopyExceptionFactory.permission();
			}
		} catch (IOException e) {
			e.printStackTrace();
			int value = new FolderAccessErrorDialog().showDialog(SM.frame, copiableFile.getDest().getParent());
			if (value == JOptionPane.NO_OPTION) {
				throw CopyExceptionFactory.notFound();
			}
		}
		return null;
	}

	/**
	 * Attempts to transfer a chunk of data between {@link FileChannel}s and returns the amount of data transferred.
	 * If an exception occurs an error dialog will be shown and the method will return -1.
	 * 
	 * @post This function guarantees to return a value between [-1, count]
	 * 
	 * @param input FileChannel in read mode
	 * @param output FileChannel in write mode
	 * @param pos Position at which start to copy
	 * @param count number of bytes to be transferred
	 * @return The amount of bytes transferred or -1 if there was an error.
	 * @throws CopyException 
	 */
	private long transfer(FileChannel input, FileChannel output, long pos, long count) throws CopyException {
		try {
			copiableFile.setCopyPosition(pos);
			long transfer =  input.transferTo(pos, count, output);
			
			return transfer;
		} catch (IOException e) {
			int value = new WriteErrorDialog().showDialog(SM.frame, copiableFile.getTemp());
			if (value == JOptionPane.NO_OPTION || value == JOptionPane.DEFAULT_OPTION) {
				throw CopyExceptionFactory.write();
			}
		}
		return -1;
	}	

	/**
	 * Attempts to close a channel if it's not null.
	 * @param channel FileChannel to be closed
	 */
	private void closeChannel(FileChannel channel) {
		if (channel != null)
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * Pauses the copy
	 */
	private void pause() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles the transfer between two {@link FileChannel}s. If an exception occurs the transfer will finish
	 * and false will be returned.
	 * @param input FileChannel in read mode
	 * @param output FileChannel in write mode
	 * @return true if the transfer completed, false otherwise
	 */
	public boolean doCopy(FileChannel input, FileChannel output) throws CopyException{
		CopyTimer timer = new CopyTimer();
		final long size = copiableFile.getSize();
		long pos = 0;
		try {
			pos = output.position();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long count = 0;
		double speed;

		while (pos < size) {
			SM.buffer.resetTime();
			if (copiableFile.getConflictAction() == ConflictAction.SKIP) {
				return true;  // assumes the copy was complete
			}
			while (SM.isPaused()) {
				pause();
			}
			SM.buffer.resetTime();

			final long remain = size - pos;
			count = Math.min(remain, SM.buffer.getBufferSize());
			final long bytesCopied = transfer(input, output, pos, count);
			if (bytesCopied<0) { // there was an error
				return false;
			}

			if (SM.hasGUI()) {;
			InfoChunkMap chunks = new InfoChunkMap();
			chunks.createChunk("bytesCopied", bytesCopied);
			speed = SM.buffer.getSpeedSeconds(bytesCopied);
			chunks.createChunk("speed", speed);
			timer.calcAvgSpeed(speed);
			if (!timer.isCalculating()) {
				chunks.createChunk("currentTimeLeft", timer.getTime(copiableFile.getSize()-remain));
				chunks.createChunk("totalTimeLeft", timer.getTime(SM.getTotalSizeLeft()));
				chunks.createChunk("calculatingTime", false);
			} else {
				chunks.createChunk("calculatingTime", true);
			}
			publish(chunks);
			}

			pos += bytesCopied;
		}
		return true;
	}

	@Override
	public Boolean doInBackground() throws Exception {
		boolean success = false;

		final FileChannel input = loadInputChannel(copiableFile.getCopyPosition());
		final FileChannel output = loadOutputChannel(copiableFile.getCopyPosition());
		if (input == null || output == null) {
			closeChannel(input);
			closeChannel(output);
			return false;
		}
		try  {
			success =  doCopy(input, output);
		} finally {
//			outputLock.release();
			if (input != null) input.close();
			if (output != null) output.close();
		}
		return success;
	}

	@Override
	public void process(List<Object> allChunks) {
		for (Object chunksMap:allChunks) {
			InfoChunkMap chunks = (InfoChunkMap) chunksMap;
			SM.addCopiedBytes(chunks.getChunkValue("bytesCopied"));
			SM.setSpeed(chunks.getChunkValue("speed"));

			if (!((boolean) chunks.getChunkValue("calculatingTime"))) {
				SM.setCalculatingTimeLeft(false);
				SM.setCurrentTimeLeft(chunks.getChunkValue("currentTimeLeft"));
				SM.setTotalTimeLeft(chunks.getChunkValue("totalTimeLeft"));
			}

		}
	}

}
