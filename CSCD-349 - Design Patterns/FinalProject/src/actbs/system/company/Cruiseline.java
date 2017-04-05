package actbs.system.company;

import actbs.system.location.Location;
import actbs.system.trip.Cruise;
import actbs.system.trip.sections.SectionType;
import actbs.system.trip.sections.berths.BerthClass;
import actbs.system.vehicle.Ship;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cruiseline extends Company {
	private final HashMap<String, Integer> defaultFamilyPrice;
	private final HashMap<String, Integer> defaultFamilyDeluxePrice;
	private final HashMap<String, Integer> defaultCouplesPrice;
	private final HashMap<String, Integer> defaultCouplesDeluxePrice;
	private final HashMap<String, Ship> ships;

	public Cruiseline(String name) {
		super(name);
		defaultFamilyPrice = new HashMap<>();
		defaultFamilyDeluxePrice = new HashMap<>();
		defaultCouplesPrice = new HashMap<>();
		defaultCouplesDeluxePrice = new HashMap<>();
		ships = new HashMap<>();
	}

	public void createSection(String shipID, BerthClass berthClass, SectionType sectionType, int rows, int price) throws ParseException {
		if (ships.containsKey(shipID)) {
			Ship ship = ships.get(shipID);
			ship.createSection(sectionType, rows, berthClass, price);
			return;
		}
		System.out.println("Cannot create section in non-existent trip");
	}

	public void createShip(String name) {
		ships.put(name, new Ship(name));
	}

	public void createTrip(Location orig, ArrayList<Location> destinations, Date departureDate, Date arrivalDate, String cruiseID) {
		Ship ship = findAvailableShip(departureDate, arrivalDate);
		if (ship != null)
			trips.put(cruiseID, new Cruise(orig, destinations, cruiseID, departureDate, arrivalDate, ship));
	}

	public void createTrip(Location orig, ArrayList<Location> destinations, Date departureDate, Date arrivalDate, String cruiseID, String shipID) {
		if (ships.containsKey(shipID))
			trips.put(cruiseID, new Cruise(orig, destinations, cruiseID, departureDate, arrivalDate, ships.get(shipID)));
	}

	private Ship findAvailableShip(Date departureDate, Date arrivalDate) {
		Date cruiseDepartureDate;
		Date cruiseArrivalDate;
		boolean shipAvailable = true;
		for (Map.Entry entry : ships.entrySet()) {
			Ship ship = (Ship) entry.getValue();
			for (Cruise cruise : ship.getCruises()) {
				cruiseDepartureDate = cruise.getDepartureDate();
				cruiseArrivalDate = cruise.getArrivalDate();
				if (!((cruiseDepartureDate.before(departureDate) && cruiseArrivalDate.before(arrivalDate)) || cruiseArrivalDate.after(arrivalDate))) {
					shipAvailable = false;
					break;
				}
			}
			if (shipAvailable) return ship;
		}
		System.out.println("There were no available ships on the given dates");
		return null;
	}

	@Override
	public void setDefaultPrice(String orig, ArrayList<String> destinations, BerthClass seatClass, int price) {
		String dest = "";
		for (String s : destinations) {
			dest += s;
		}
		switch (seatClass) {
			case FA:
				if (!defaultFamilyPrice.containsKey(orig + "-" + dest))
					defaultFamilyPrice.put(orig + "-" + dest, price);
				break;
			case DF:
				if (!defaultFamilyDeluxePrice.containsKey(orig + "-" + dest))
					defaultFamilyDeluxePrice.put(orig + "-" + dest, price);
				break;
			case C:
				if (!defaultCouplesPrice.containsKey(orig + "-" + dest))
					defaultCouplesPrice.put(orig + "-" + dest, price);
				break;
			case DC:
				if (!defaultCouplesDeluxePrice.containsKey(orig + "-" + dest))
					defaultCouplesDeluxePrice.put(orig + "-" + dest, price);
				break;
		}
	}

	@Override
	public String getExtraInfo() {
		if (ships.isEmpty()) {
			return "()";
		}
		String out = "(";
		for (Map.Entry entry : ships.entrySet()) {
			Ship ship = (Ship) entry.getValue();
			out += ship.toString() + ",";
		}
		return out.substring(0, out.length() - 1) + ")";
	}
}
