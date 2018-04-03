package actbs.system.company;

import actbs.system.trip.Trip;
import actbs.system.trip.sections.SectionType;
import actbs.system.trip.sections.berths.BerthClass;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Company implements ICompany {
	final HashMap<String, Trip> trips;
	private final String name;

	Company(String name) {
		this.name = name;
		trips = new HashMap<>();
	}

	private String getName() {
		return name;
	}

	public void createSection(String tripID, BerthClass berthClass, SectionType sectionType, int rows, int price) throws ParseException {
		if (trips.containsKey(tripID)) {
			Trip trip = trips.get(tripID);
			setDefaultPrice(trip.getOrigin().getName(), trip.getDestinationsAsStrings(), berthClass, price);
			trip.createSection(sectionType, rows, berthClass, price);
			return;
		}
		System.out.println("Cannot create section in non-existent trip");
	}

	public void bookBerth(String tripID, BerthClass berthClass, int row, int col) {
		if (trips.containsKey(tripID)) {
			trips.get(tripID).bookBerth(berthClass, row, col);
		} else {
			System.out.println("Cannot book Berth on non-existent trip");
		}
	}

	public void bookAnyBerth(String tripID, BerthClass berthClass) {
		if (trips.containsKey(tripID)) {
			trips.get(tripID).bookAnyBerth(berthClass);
		} else {
			System.out.println("Cannot book Berth in non-existent trip");
		}
	}

	public ArrayList<Trip> findAvailableTrips(String orig, ArrayList<String> destinations) {
		ArrayList<Trip> availableTrips = new ArrayList<>();
		for (Map.Entry entry : trips.entrySet()) {
			Trip trip = (Trip) entry.getValue();
			if (trip.getOrigin().getName().equals(orig) && destinationsAreEqual(trip.getDestinationsAsStrings(), destinations) && trip.hasAvailableBerths()) {
				availableTrips.add(trip);
			}
		}
		return availableTrips;
	}

	private boolean destinationsAreEqual(ArrayList<String> destinations1, ArrayList<String> destinations2) {
		return destinations1.containsAll(destinations2) && destinations2.containsAll(destinations1);
	}

	public void overridePrice(String tripID, BerthClass seatClass, int price) {
		if (isValidTripID(tripID)) {
			trips.get(tripID).overridePrice(seatClass, price);
		} else {
			System.out.println("Could not override price, invalid trip ID");
		}
	}

	public boolean isValidTripID(String tripID) {
		return (!trips.containsKey(tripID)) && tripID.matches("[A-Z|0-9]+");
	}

	public String toString(boolean detailMode) {
		String out = getName() + getExtraInfo() + "[";
		if (trips.isEmpty())
			return out + "]";
		for (Map.Entry entry : trips.entrySet()) {
			Trip trip = (Trip) entry.getValue();
			out += trip.toString(detailMode) + ",";
		}
		return out.substring(0, out.length() - 1) + "]";
	}

	public String toString() {
		return toString(false);
	}
}
