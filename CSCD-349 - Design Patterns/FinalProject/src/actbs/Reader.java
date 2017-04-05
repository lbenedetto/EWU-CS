package actbs;

import actbs.system.trip.sections.SectionType;
import actbs.system.trip.sections.berths.BerthClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

class Reader {
	private static final Scanner kb = new Scanner(System.in);

	static boolean readPreference(String type) {
		System.out.print("Would you prefer a" + type + " seat? y/n");
		String in = readString();
		return in.charAt(0) == 'y';
	}

	static String readSeatLocation() {
		System.out.print("Enter seat ID: ");
		String seatID = null;
		while (seatID == null) {
			seatID = readString();
			if (!seatID.matches("^[0-9]{1,3}[A-J]$")) {
				seatID = null;
				System.out.print("Invalid seatID, try again: ");
			}
		}
		return seatID;
	}

	static Date readDate(String type) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy,MM,dd,kk,mm");
		dateFormat.setLenient(false);
		System.out.print("Enter " + type + " date (yyyy,mm,dd,HH,MM): ");
		String dateStr = readString();
		Date d = null;
		if (dateStr.equals("*.*"))
			d = new Date();
		while (d == null) {
			try {
				d = dateFormat.parse(dateStr);
			} catch (ParseException e) {
				System.out.print("Failed to parse date, try again (Use format yyyy,mm,dd,HH,MM): ");
				dateStr = readString();
			}
		}
		return d;
	}

	static String readLocation(String type) {
		System.out.print("Enter " + type + ": ");
		return readString();
	}

	static ArrayList<String> readDestinationArray() {
		ArrayList<String> destinations = new ArrayList<>();
		System.out.println("Type \"DONE\" to stop entering destinations");
		String temp;
		while (true) {
			temp = readLocation("destination");
			if(temp.equalsIgnoreCase("done")) break;
			destinations.add(temp);
		}
		Collections.sort(destinations);
		return destinations;
	}

	static ArrayList<String> readSingleDestinationAsArray() {
		ArrayList<String> temp = new ArrayList<>();
		temp.add(readLocation("destination"));
		return temp;
	}

	static String readFilename() {
		System.out.print("Enter filename: ");
		return readString();
	}

	static String readID(String type) {
		System.out.print("Enter " + type + " ID: ");
		return readString();
	}

	static String readCompany() {
		System.out.print("Enter company name: ");
		return readString();
	}

	static SectionType readSectionType() {
		System.out.print("Enter section type: ");
		SectionType sectionType = null;
		while (sectionType == null) {
			try {
				sectionType = SectionType.valueOf(readString());
			} catch (IllegalArgumentException e) {
				System.out.print("Invalid section type, try again: ");
			}
		}
		return sectionType;
	}

	static BerthClass readBerthClass(String options) {
		if (options.equals("airport")) options = "(F,B,E)";
		else if (options.equals("seaport")) options = "(FA,DF,C,DC)";
		System.out.print("Enter berth class " + options + ": ");
		BerthClass seatClass = null;
		while (seatClass == null) {
			try {
				seatClass = BerthClass.valueOf(readString());
			} catch (IllegalArgumentException e) {
				System.out.print("Invalid seat class, try again: ");
			}
		}
		return seatClass;
	}

	private static String readString() {
		while (!kb.hasNextLine()) {
			kb.next();
		}
		return kb.nextLine();
	}

	static int readInt(String type) {
		System.out.print("Enter " + type + ": ");
		while (!kb.hasNextInt()) {
			kb.next();
		}
		int out = kb.nextInt();
		kb.nextLine();
		return out;
	}
}
