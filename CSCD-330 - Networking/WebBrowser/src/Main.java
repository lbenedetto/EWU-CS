import java.io.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {
		scrapeAndSave("http://stackoverflow.com/questions/3024654/catch-a-generic-exception-in-java");
		scrapeAndSave("https://en.wikipedia.org/asdfasdfa");
	}

	private static void scrapeAndSave(String siteURL) {
		String HTML = scrapeLink(siteURL);
		if (HTML == null) return;
		saveStringToFile(HTML);
	}

	private static String scrapeLink(String siteURL) {
		String HTML = "";
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;

		try {
			url = new URL(siteURL);
			is = url.openStream();
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				HTML += line + " ";
			}
		} catch (FileNotFoundException fnfe) {
			//If the page 404's it will most likely give a FileNotFoundException instead of returning an error code
			System.out.println(getCodeMessage("404"));
		} catch (IOException ioe) {
			String message = ioe.getMessage();
			String out = handleHTTPStatusCode(message);
			if (out == null) {
				ioe.printStackTrace();
			} else {
				System.out.println(out);
			}
			return null;
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return HTML;
	}

	private static void saveStringToFile(String html) {
		PrintWriter txtFile;
		//Create the directory
		File f = new File("./downloads/");
		if (!f.exists())
			if (!f.mkdirs())
				System.out.println("Failed to create directory ./downloads/");
		//Try to write to the file.
		try {
			String pageTitle = "UnknownPageTitle";
			Matcher m = Pattern.compile("(?:<title>)(.*?)(?:</title>)").matcher(html);
			if (m.find()) {
				pageTitle = m.group(1);
			}
			txtFile = new PrintWriter(new FileWriter("./downloads/" + pageTitle + ".html", true));
			txtFile.println(html);
			txtFile.close();
		} catch (IOException ex) {
			System.out.println("Something prevented the program from saving the file");
			System.exit(-1);
		}

	}

	private static String handleHTTPStatusCode(String message) {
		if (message.contains("HTTP response code:")) {
			String errorCode;
			Matcher m = Pattern.compile("(?:HTTP response code: )(\\d{3})").matcher(message);
			if (m.find()) {
				errorCode = m.group(1);
				return getCodeMessage(errorCode);
			}
		}
		return null;
	}

	private static String getCodeMessage(String errorCode) {
		try {
			String codeMessage = "";
			IterableFile file = new IterableFile("./HTTP/ErrorCodes.txt");
			boolean recording = false;
			for (String s : file) {
				if (s.startsWith(errorCode)) {
					recording = true;
					codeMessage += s + "\n";
				} else if (recording) {
					if (s.matches("\\d{3}.*?:$")) {
						recording = false;
					} else {
						codeMessage += s + "\n";
					}
				}
			}
			return codeMessage;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
