package actbs.system.company;

import actbs.system.location.Location;
import actbs.system.trip.Flight;
import actbs.system.trip.sections.berths.BerthClass;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Airline extends Company {
	private final HashMap<String, Integer> defaultEconomyPrice;
	private final HashMap<String, Integer> defaultBusinessPrice;
	private final HashMap<String, Integer> defaultFirstPrice;

	public Airline(String name) {
		super(name);
		defaultEconomyPrice = new HashMap<>();
		defaultBusinessPrice = new HashMap<>();
		defaultFirstPrice = new HashMap<>();
	}

	public void createTrip(Location orig, ArrayList<Location> destinations, Date departureDate, Date arrivalDate, String flightID) {
		if (((arrivalDate.getTime() - departureDate.getTime()) / 1000.0) <= 86400.0) {
			trips.put(flightID, new Flight(orig, destinations, flightID, departureDate, arrivalDate));
		} else {
			System.out.println("Could not create flight, flight was longer than 24 hours");
		}
	}

	public void bookSeatWithPreferences(String flightID, BerthClass seatClass, boolean preferWindow, boolean preferAisle) {
		if (trips.containsKey(flightID)) {
			((Flight) trips.get(flightID)).bookSeatWithPreferences(seatClass, preferWindow, preferAisle);
		} else {
			System.out.println("Cannot book seat on non-existent flight");
		}
	}

	public void setDefaultPrice(String orig, ArrayList<String> destinations, BerthClass seatClass, int price) {
		String dest = "";
		for (String s : destinations) {
			dest += s;
		}
		switch (seatClass) {
			case F:
				if (!defaultFirstPrice.containsKey(orig + "-" + dest))
					defaultFirstPrice.put(orig + "-" + dest, price);
				break;
			case B:
				if (!defaultBusinessPrice.containsKey(orig + "-" + dest))
					defaultBusinessPrice.put(orig + "-" + dest, price);
				break;
			case E:
				if (!defaultEconomyPrice.containsKey(orig + "-" + dest))
					defaultEconomyPrice.put(orig + "-" + dest, price);
				break;
		}
	}

	@Override
	public String getExtraInfo() {
		return "";
	}
}
