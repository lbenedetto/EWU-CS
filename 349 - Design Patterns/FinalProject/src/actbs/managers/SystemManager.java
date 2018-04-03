package actbs.managers;

import actbs.system.AMSParser;
import actbs.system.company.Company;
import actbs.system.location.Location;
import actbs.system.trip.Trip;
import actbs.system.trip.sections.SectionType;
import actbs.system.trip.sections.berths.BerthClass;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SystemManager implements IManager {
	final HashMap<String, Location> locations;
	final HashMap<String, Company> companies;

	SystemManager() {
		locations = new HashMap<>();
		companies = new HashMap<>();
	}

	public ArrayList<Trip> findAvailableTrips(String orig, ArrayList<String> dest, Date departureDate, BerthClass berthClass) {
		ArrayList<Trip> availableTrips = findAvailableTrips(orig, dest);
		ArrayList<Trip> matchingTrips = new ArrayList<>();
		for (Trip trip : availableTrips) {
			if (trip.departsOn(departureDate)) {
				if (trip.hasAvailableBerths(berthClass)) {
					matchingTrips.add(trip);
				}
			}
		}
		return matchingTrips;
	}

	private ArrayList<Trip> findAvailableTrips(String orig, ArrayList<String> dest) {
		ArrayList<Trip> availableTrips = new ArrayList<>();
		for (Map.Entry entry : companies.entrySet()) {
			Company company = (Company) entry.getValue();
			availableTrips.addAll(company.findAvailableTrips(orig, dest));
		}
		return availableTrips;
	}

	public void bookBerth(String company, String tripID, BerthClass berthClass, String ID) {
		Matcher matcher = Pattern.compile("[0-9]{1,3}").matcher(ID);
		if (matcher.find()) {
			int row = Integer.parseInt(matcher.group(0));
			int col = ID.replaceAll("[0-9]", "").charAt(0) - 64;
			if (companies.containsKey(company)) {
				companies.get(company).bookBerth(tripID, berthClass, row, col);
			} else {
				System.out.println("Could not book Berth in non-existent company");
			}
		} else {
			System.out.println("Invalid seat ID");
		}
	}

	public void bookAnyBerth(String company, String tripID, BerthClass berthClass) {
		if (companies.containsKey(company)) {
			companies.get(company).bookAnyBerth(tripID, berthClass);
		} else {
			System.out.println("Could not book Berth in non-existent company");
		}
	}

	public void createTrip(String companyName, String tripID, Date departureDate, Date arrivalDate, String orig, ArrayList<String> destinations) {
		if (tripIsValid(companyName, orig, destinations, tripID, departureDate, arrivalDate)) {
			ArrayList<Location> matchedDestinations = new ArrayList<>();
			for (String dest : destinations) {
				matchedDestinations.add(locations.get(dest));
			}
			companies.get(companyName).createTrip(locations.get(orig), matchedDestinations, departureDate, arrivalDate, tripID);
		}
	}

	public void showAvailableTrips(ArrayList<Trip> availableTrips) {
		if (availableTrips.size() > 0) {
			for (Trip trip : availableTrips) {
				System.out.println(trip.toString());
			}
		} else {
			System.out.println("There are no trips to show");
		}
	}

	public void setDefaultPrice(String company, String orig, ArrayList<String> dest, BerthClass berthClass, int price) {
		if (companies.containsKey(company)) {
			companies.get(company).setDefaultPrice(orig, dest, berthClass, price);
		} else {
			System.out.println("Could not set price, no such company");
		}
	}

	public void overridePrice(String company, String tripID, BerthClass berth, int price) {
		if (companies.containsKey(company)) {
			companies.get(company).overridePrice(tripID, berth, price);
		} else {
			System.out.println("Could not set price, no such company");
		}
	}

	public void createSection(String company, String tripID, BerthClass berthClass, SectionType sectionType, int rows, int price) throws ParseException {
		if (companies.containsKey(company)) {
			companies.get(company).createSection(tripID, berthClass, sectionType, rows, price);
			return;
		}
		System.out.println("Could not create section in non-existent company");
	}

	boolean tripIsValid(String companyName, String orig, ArrayList<String> destinations, String id, Date departureDate, Date arrivalDate) {
		String fail = "Could not create trip. ";
		double deltaDate = ((arrivalDate.getTime() - departureDate.getTime()) / 1000.0 / 60.0);
		if (deltaDate > 30240.0) {
			System.out.println(fail + "Trip is longer than 21 days");
			return false;
		} else if (deltaDate <= 1) {
			System.out.println(fail + "Trip is too short, or arrives before it departs. System does not yet support booking of time travel based vacations");
			return false;
		} else if (!companies.containsKey(companyName)) {
			System.out.println(fail + "Company \"" + companyName + "\" does not exist.");
			return false;
		} else if (!locations.containsKey(orig)) {
			System.out.println(fail + "Location \"" + orig + "\" does not exist.");
			return false;
		} else if (!companies.get(companyName).isValidTripID(id)) {
			System.out.println(fail + "trip ID \"" + id + "\" is not a valid trip ID");
			return false;
		}
		for (String dest : destinations) {
			if (!locations.containsKey(dest)) {
				System.out.println(fail + "Location \"" + dest + "\" does not exist.");
				return false;
			}
		}
		return true;
	}

	boolean isLocationNameValid(String name) {
		String fail = "Could not create location with name \"" + name + "\" ";
		name = name.toUpperCase();
		if (name.length() != 3) {
			System.out.println(fail + "Location name must be 3 characters.");
			return false;
		} else if (name.matches("[^A-Z]")) {
			System.out.println(fail + "Location name must contain only characters.");
			return false;
		} else if (locations.containsKey(name)) {
			System.out.println(fail + "Location with that name already exists.");
			return false;
		}
		return true;
	}

	boolean isCompanyNameValid(String name) {
		if (name.length() > 5) {
			System.out.println("Could not create company with name \"" + name + "\". Company name must be less than 6 characters.");
			return false;
		} else if (companies.containsKey(name)) {
			System.out.println("Could not create company with name \"" + name + "\". Company already exists.");
			return false;
		}
		return true;
	}

	public void loadSystemDetails(String filename) {
		String input;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
			input = bufferedReader.readLine();
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + filename);
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		AMSParser parser = new AMSParser(this);
		parser.parse(input);
	}

	public void saveSystemDetails(String filename) {
		try {
			File file = new File(filename);
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(getSystemDetails(false));
			fileWriter.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void displaySystemDetails() {
		System.out.println(getSystemDetails(false));
	}

	public void displayDetailedSystemReport() {
		System.out.println(getSystemDetails(true));

	}

	private String getSystemDetails(boolean detailMode) {
		String out = "[";
		if (locations.isEmpty()) {
			return "[]";
		}
		for (Map.Entry entry : locations.entrySet()) {
			Location location = (Location) entry.getValue();
			out += location.getName() + ",";
		}
		out = out.substring(0, out.length() - 1) + "]{";
		if (companies.isEmpty()) {
			return out + "}";
		}
		for (Map.Entry entry : companies.entrySet()) {
			Company company = (Company) entry.getValue();
			out += company.toString(detailMode) + ",";
		}
		out = out.substring(0, out.length() - 1) + "}";
		return out;
	}
}
