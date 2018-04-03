package actbs.system.company;

import actbs.system.location.Location;
import actbs.system.trip.sections.berths.BerthClass;

import java.util.ArrayList;
import java.util.Date;

interface ICompany {
	void createTrip(Location orig, ArrayList<Location> destinations, Date departureDate, Date arrivalDate, String flightID);

	void setDefaultPrice(String orig, ArrayList<String> dest, BerthClass seatClass, int price);

	String getExtraInfo();
}
