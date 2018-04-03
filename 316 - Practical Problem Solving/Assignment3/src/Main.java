import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author Lars Benedetto
 * Assignment 3, line counter
 * Extra credit attempted
 */
public class Main {
	private static final boolean logging = true;

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		System.out.print("Enter absolute path to directory: ");
		String directory = kb.nextLine();
		System.out.print("Enter file extension: ");
		String extension = kb.nextLine();
		int total = 0;
		try {
			ArrayList<IterableFile> files = getAllFiles(directory, extension);
			for (IterableFile f : files) {
				int lines = 0;
				String fileString = "";
				for (String s : f) {
					fileString += s + "\n";
				}
				fileString = removeComments(fileString);
				Matcher m = Pattern.compile("\\s").matcher(fileString);
				while (m.find()) lines++;
				System.out.println(f.fileName + " - lines: " + lines);
				total += lines;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Total: " + total);
	}

	private static String removeComments(String s) {
		//Replace spaces with nothing
		s = s.replaceAll(" ", "");
		//Get rid of tab characters
		s = s.replaceAll("\\t", "");
		//Replace newlines with spaces
		s = s.replaceAll("\\n", " ");
		//Now whitespace can be interpreted as newlines, which makes it easier to use .* to match stuff
		//Replace anything in quotes with empty quotes
		s = s.replaceAll("\".*?\"", "\"\"");
		//Now we replace any single line comments
		s = s.replaceAll("//.*?(?=\\s)", "");
		//Now replace any multiline comments
		s = s.replaceAll("/\\*.*?\\*/", "");
		//Now replace any blank lines (double newlines)
		s = s.replaceAll("\\s{2,}", " ");
		//Remove space at beginning of string, if any
		s = s.replaceAll("^\\s", "");
		return s;
	}

	/**
	 * Recursively gets an ArrayList of all files in the directory and subdirectories
	 *
	 * @param directory String
	 * @param extension String
	 * @return ArrayList
	 * @throws FileNotFoundException
	 */
	private static ArrayList<IterableFile> getAllFiles(String directory, String extension) throws FileNotFoundException {
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();
		ArrayList<IterableFile> out = new ArrayList<>();
		if (listOfFiles == null) throw new FileNotFoundException("Couldn't find files");
		for (File f : listOfFiles) {
			if (f.isFile() && f.getName().endsWith(extension)) {
				if (logging) System.out.println(f.getAbsolutePath());
				out.add(new IterableFile(f.getAbsolutePath()));
			} else if (f.isDirectory()) {
				out.addAll(getAllFiles(f.getAbsolutePath(), extension));
			}
		}
		return out;
	}
}
