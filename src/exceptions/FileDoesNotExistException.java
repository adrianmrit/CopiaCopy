package exceptions;

public class FileDoesNotExistException extends Exception{
	/**
	 * final serialVersionUID 
	 */
	private static final long serialVersionUID = -1927128383023401033L;

	public FileDoesNotExistException(String message) {
		super(message);
	}
}
