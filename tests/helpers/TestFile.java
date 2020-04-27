package helpers;

import static org.junit.jupiter.api.Assertions.fail;

public class TestFile {
	private static final String FILE_MUST_EXIST = "File must exist for tests to work: %s";
	private static final String FILE_MUST_NOT_EXIST = "File must not exist for tests to work: %s";
	private TestFile() {};
	
	/**
	 * unit test fail with information message
	 * @param path
	 */
	public static void fileExistsFailed(String path) {
		fail(String.format(FILE_MUST_EXIST, path));
	}
	
	/**
	 * unit test fail with information message
	 * @param path
	 */
	public static void fileDoesntExistFailed(String path) {
		fail(String.format(FILE_MUST_NOT_EXIST, path));
	}
}
