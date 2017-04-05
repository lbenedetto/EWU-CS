package actbs.system.trip;

import actbs.system.location.Location;
import actbs.system.trip.sections.SectionType;
import actbs.system.trip.sections.TripSection;
import actbs.system.trip.sections.berths.BerthClass;
import actbs.system.vehicle.Ship;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class Cruise extends Trip implements Observer {
	private final Ship ship;

	public Cruise(Location orig, ArrayList<Location> destinations, String cruiseID, Date departureDate, Date arrivalDate, Ship ship) {
		super(orig, destinations, cruiseID, departureDate, arrivalDate, ship.getSections());
		this.ship = ship;
	}

	@Override
	public void createSection(SectionType sectionType, int rows, BerthClass berthClass, int price) throws ParseException {
		ship.createSection(sectionType, rows, berthClass, price);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		sections = (ArrayList<TripSection>) arg;
	}

	@Override
	public String getExtraInfo() {
		return "|" + ship.getShipID();
	}
}
