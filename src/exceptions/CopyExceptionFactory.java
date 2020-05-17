package exceptions;

import languages.LangBundle;

public class CopyExceptionFactory{
	
	public static CopyException copyIntoItself() {
		return new CopyException(LangBundle.CURRENT.getString("copyIntoItselfException"));
	}
	
	public static CopyException nameConflict() {
		return new CopyException(LangBundle.CURRENT.getString("nameConflictException"));
	}
	
	public static CopyException notFound() {
		return new CopyException(LangBundle.CURRENT.getString("pathNotFoundException"));
	}
	
	public static CopyException permission() {
		return new CopyException(LangBundle.CURRENT.getString("permissionException"));
	}
	
	public static CopyException write() {
		return new CopyException(LangBundle.CURRENT.getString("writeException"));
	}

}
