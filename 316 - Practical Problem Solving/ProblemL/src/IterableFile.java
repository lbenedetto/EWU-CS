import java.io.*;
import java.util.Iterator;

/**
 * https://github.com/larperdoodle/CaptainMarkov/blob/master/src/main/java/CaptainMarkov/utils/IterableFile.java
 */
public class IterableFile implements Iterable<String> {
	private BufferedReader file;
	public String fileName;

	public IterableFile(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
		file = new BufferedReader(new FileReader(fileName));
	}

	@Override
	public FileIterator iterator() {
		FileIterator fi = new FileIterator();
		fi.next();
		return fi;
	}

	private class FileIterator implements Iterator<String> {
		String nextLine;
		String curr;


		@Override
		public boolean hasNext() {
			return nextLine != null;
		}

		@Override
		public String next() {
			curr = nextLine;
			try {
				nextLine = file.readLine();
				while (nextLine.equals(""))
					nextLine = file.readLine();
			} catch (IOException | NullPointerException e) {
				nextLine = null;
			}
			return curr;
		}
	}
}