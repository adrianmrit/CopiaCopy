package exceptions;

public class RecursionAttemptException extends Exception {

	/**
	 * final serialVersionUID
	 */
	private static final long serialVersionUID = 8066026146538287959L;
	
	public RecursionAttemptException(String message) {
		super(message);
	}
	
	public RecursionAttemptException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RecursionAttemptException(Throwable cause) {
		super(cause);
	}

}
