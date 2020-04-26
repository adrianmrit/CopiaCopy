package copy;

import org.apache.commons.io.FileUtils;

public class SizeRep {
	private SizeRep() {}
	
	public static final double BSize = 1;
	public static final double KBSize = FileUtils.ONE_KB;
	public static final double MBSize = FileUtils.ONE_MB;
	public static final double GBSize = FileUtils.ONE_GB;
	public static final double TBSize = FileUtils.ONE_TB;
	
	public static final String BRep = "b";
	public static final String KBRep = "kb";
	public static final String MBRep = "mb";
	public static final String GBRep = "gb";
	public static final String TBRep = "tb";
	
	
	/**
	 * Converts bytes to kilobytes
	 * @param b size in bytes
	 * @return size in KB
	 */
	public static double toKB(long b) {
		return (double) b/KBSize;
	}
	
	/**
	 * Converts bytes to megabytes
	 * @param b size in bytes
	 * @return size in MB
	 */
	public static double toMB(long b) {
		return (double) b/MBSize;
	}
	
	/**
	 * Converts bytes to gigabytes
	 * @param b size in bytes
	 * @return size in GB
	 */
	public static double toGB(long b) {
		return (double) b/GBSize;
	}
	
	/**
	 * Converts bytes to terabytes
	 * @param b size in bytes
	 * @return size in GB
	 */
	public static double toTB(long b) {
		return (double) b/TBSize;
	}
	
	public static double to(String rep, long b) {
		if (rep.equals(KBRep)) {
			return toKB(b);
		} else if (rep.equals(MBRep)) {
			return toMB(b);
		} else if (rep.equals(GBRep)) {
			return toGB(b);
		} else if (rep.equals(TBRep)){
			return toTB(b);
		} else{
			return BSize;
		}
	}
	
	public static int bestIntValFactor(long b) {
		int maxIntVal = Integer.MAX_VALUE;
		long rep = b;
		int power = 0;
		double factor = Math.pow(10, power);
		while (rep > maxIntVal) {
			power += 3;
			factor = Math.pow(10, power);
			rep /= factor;
		}
		
		return (int) factor;
	}
	
	
	/**
	 * Returns the best way to represent the value,
	 * in kilobytes, megabytes, gigabytes or terabytes.
	 * @param b size in bytes
	 * @return String representation
	 */
	public static String readableRep(long b) {
		if (b <= KBSize) {
			return BRep;
		} else if (b <= MBSize) {
			return KBRep;
		} else if (b <= GBSize) {
			return MBRep;
		} else if (b <= TBSize){
			return GBRep;
		} else{
			return TBRep;
		}
	}
	
	/**
	 * Returns the converted value in best way to represent the value,
	 * in kilobytes, megabytes, gigabytes or terabytes.
	 * @param b size in bytes
	 * @return Value in kilobytes, megabytes, gigabytes or terabytes
	 */
	public static double readableVal(long b) {
		if (b <= KBSize) {
			return (double) b;
		} else if (b <= MBSize) {
			return toKB(b);
		} else if (b <= GBSize) {
			return toMB(b);
		} else if (b <= TBSize){
			return toGB(b);
		} else{
			return toTB(b);
		}
	}
}
