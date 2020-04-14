package copy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SampleFile {
	private final static byte b = 0;
	private final static int CHUNK_SIZE = 1073741824; //1Gb
	private final static long FILE_SIZE = 1073741824; //1Gb
	private final static File SAMPLE_FILE = new File("SampleFile.sample");
	public static void main(String args[]) throws IOException {
		long size = 0;
		byte[] chunk = new byte[CHUNK_SIZE];
		
		for (int i=0; i<CHUNK_SIZE; i++) {
			chunk[i] = b;
		}
		System.out.println("Writting sample file");
		while (size <= FILE_SIZE){
			OutputStream os = new FileOutputStream(SAMPLE_FILE);
			size += FILE_SIZE;
			try {
				os.write(chunk, 0, chunk.length);
			}finally {
				os.close();
			}
		}
		
		System.out.println("finished");
	}
}
