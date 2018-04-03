package actbs.managers;

import actbs.system.company.Airline;
import actbs.system.location.Airport;
import actbs.system.trip.sections.berths.BerthClass;

public class AirportSystemManager extends SystemManager implements IManager {
	public AirportSystemManager() {
		super();
	}

	@Override
	public void createLocation(String name) {
		if (isLocationNameValid(name))
			locations.put(name, new Airport(name));
	}

	@Override
	public void createCompany(String name) {
		if (isCompanyNameValid(name))
			companies.put(name, new Airline(name));
	}

	public void bookSeatWithPreferences(String airline, String flightID, BerthClass seatClass, boolean preferWindow, boolean preferAisle) {
		if (companies.containsKey(airline)) {
			((Airline) companies.get(airline)).bookSeatWithPreferences(flightID, seatClass, preferWindow, preferAisle);
		}
	}
}
