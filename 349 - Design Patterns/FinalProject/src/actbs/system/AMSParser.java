package actbs.system;

import actbs.managers.SeaportSystemManager;
import actbs.managers.SystemManager;
import actbs.system.trip.sections.SectionType;
import actbs.system.trip.sections.berths.BerthClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AMSParser {
	private final SystemManager systemManager;

	public AMSParser(SystemManager systemManager) {
		this.systemManager = systemManager;
	}

	public void parse(String input) {
		input = input.replaceAll(" ", "");
		try {
			input = parseLocations(input);
			parseCompanies(input);
		} catch (ParseException e) {
			System.out.println("Invalid file format: " + e.getMessage());
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Invalid file format, probably a missing comma");
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid file format: Used wrong BerthClass or SectionType");
		}
	}

	private String parseLocations(String s) throws ParseException {
		Matcher m = Pattern.compile("\\[.*?\\]").matcher(s);
		if (m.find()) {
			String[] locations = m.group(0).replaceAll("\\[|\\]", "").split(",");
			for (String location : locations) {
				systemManager.createLocation(location);
			}
			return m.replaceFirst("");
		} else {
			throw new ParseException("Error while parsing locations", 1);
		}
	}

	private void parseCompanies(String s) throws ParseException {
		char[] chars = s.toCharArray();
		String company = "";
		for (int i = 0; i < chars.length; i++) {
			char curr = chars[i];
			if (curr == '{') continue;
			if (curr == '}') return;
			if (curr == '[' || curr == '(') {
				systemManager.createCompany(company);
				if (curr == '(')
					i = parseShips(company, chars, i + 1);
				else
					i = parseTrips(company, chars, i + 1, true);
				company = "";
				if (chars[i] == ']') i++;
			} else {
				company += chars[i];
			}
		}
	}

	private int parseShips(String company, char[] chars, int i) throws ParseException {
		char curr = chars[i];
		if (curr == ')')
			return i + 3;//If there are no ships, there are no trips. Skip to the next company
		String shipID = "";
		while (curr != ')') {
			if (curr == '[') {
				((SeaportSystemManager) systemManager).createShip(company, shipID);
				i = parseTripSections(company, shipID, chars, i + 1) - 1;
				shipID = "";
			} else {
				shipID += curr;
			}
			i++;
			curr = chars[i];
		}
		i = parseTrips(company, chars, i + 2, false);
		return i;
	}

	private int parseTrips(String company, char[] chars, int i, boolean airplaneMode) throws ParseException {
		String[] data = new String[]{"", "", "", "", "", ""};
		char curr = chars[i];
		if (curr == ']') return i + 1;
		int dataIndex = 0;
		while (true) {
			if (curr == '|') {
				dataIndex++;
			} else if (curr == '[' || (dataIndex == 5 && curr == ',') || curr == ']') {
				ArrayList<String> destinations = new ArrayList<>();
				destinations.addAll(Arrays.asList(data[4].split(",")));
				Collections.sort(destinations);
				if (airplaneMode) {
					systemManager.createTrip(company, data[0], parseDate(data[1]), parseDate(data[2]), data[3], destinations);
					i = parseTripSections(company, data[0], chars, i + 1);
				} else {
					((SeaportSystemManager) systemManager).createTrip(company, data[0], parseDate(data[1]), parseDate(data[2]), data[3], destinations, data[5]);
				}
				data = new String[]{"", "", "", "", "", ""};
				dataIndex = 0;
				if (chars[i] == ']')
					return i + 1;
			} else {
				data[dataIndex] += curr;
			}
			i++;
			curr = chars[i];
		}
	}

	private Date parseDate(String dateStr) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("y,M,d,k,m");
		dateFormat.setLenient(false);
		return dateFormat.parse(dateStr);
	}

	private int parseTripSections(String company, String id, char[] chars, int i) throws IllegalArgumentException, ParseException {
		String[] data = new String[]{"", "", "", ""};
		char curr;
		int dataIndex = 0;
		while (true) {
			curr = chars[i];
			if (curr == ':') {
				dataIndex++;
			} else if (curr == ',' || curr == ']') {
				BerthClass berthClass = BerthClass.valueOf(data[0]);
				systemManager.createSection(company, id, berthClass, SectionType.valueOf(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[1]));
				data = new String[]{"", "", "", ""};
				dataIndex = 0;
				if (curr == ']')
					return i + 1;
			} else {
				data[dataIndex] += curr;
			}
			i++;
		}
	}
}
