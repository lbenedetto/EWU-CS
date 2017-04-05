package actbs.system.trip;

import actbs.system.location.Location;
import actbs.system.trip.sections.FlightSection;
import actbs.system.trip.sections.SectionType;
import actbs.system.trip.sections.TripSection;
import actbs.system.trip.sections.berths.BerthClass;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Flight extends Trip {
	public Flight(Location origin, ArrayList<Location> destinations, String flightID, Date departureDate, Date arrivalDate) {
		super(origin, destinations, flightID, departureDate, arrivalDate);
	}

	@Override
	public void createSection(SectionType sectionType, int rows, BerthClass berthClass, int price) throws ParseException{
		if (rows > 100) {
			System.out.println("Rows must not be greater than 100");
			return;
		}
		if (berthClass == BerthClass.FA || berthClass == BerthClass.DF || berthClass == BerthClass.C || berthClass == BerthClass.DC) {
			throw new ParseException("Wrong berth class", 3);
		}
		for (TripSection section : sections) {
			if (section.matchesBerthClass(berthClass)) {
				System.out.println("Could not create flight section. Section of that type already exists on flight");
				return;
			}
		}
		sections.add(new FlightSection(sectionType, rows, berthClass, price));
	}

	public void bookSeatWithPreferences(BerthClass seatClass, boolean preferWindow, boolean preferAisle) {
		for (TripSection section : sections) {
			if (section.matchesBerthClass(seatClass)) {
				if (section.hasAvailableBerths()) {
					((FlightSection) section).bookSeatWithPreferences(preferWindow, preferAisle);
					return;
				} else {
					System.out.println("Specified section has no available seats");
					return;
				}
			}
		}
		System.out.println("The flight had no sections of the requested type");
	}

	@Override
	public String getExtraInfo() {
		return getSectionsString();
	}
}
