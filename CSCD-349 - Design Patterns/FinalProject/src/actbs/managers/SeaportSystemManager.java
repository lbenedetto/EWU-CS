package actbs.managers;

import actbs.system.company.Cruiseline;
import actbs.system.location.Location;
import actbs.system.location.Seaport;

import java.util.ArrayList;
import java.util.Date;

public class SeaportSystemManager extends SystemManager implements IManager {

	@Override
	public void createLocation(String name) {
		if (isLocationNameValid(name))
			locations.put(name, new Seaport(name));
	}

	@Override
	public void createCompany(String name) {
		if (isCompanyNameValid(name))
			companies.put(name, new Cruiseline(name));
	}

	public void createShip(String company, String name){
		if(companies.containsKey(company))
			((Cruiseline)companies.get(company)).createShip(name);
	}

	public void createTrip(String companyName, String tripID, Date departureDate, Date arrivalDate, String orig, ArrayList<String> destinations, String shipID) {
		if (tripIsValid(companyName, orig, destinations, tripID, departureDate, arrivalDate)) {
			ArrayList<Location> matchedDestinations = new ArrayList<>();
			for (String dest : destinations) {
				matchedDestinations.add(locations.get(dest));
			}
			((Cruiseline)companies.get(companyName)).createTrip(locations.get(orig), matchedDestinations, departureDate, arrivalDate, tripID, shipID);
		}
	}
}
