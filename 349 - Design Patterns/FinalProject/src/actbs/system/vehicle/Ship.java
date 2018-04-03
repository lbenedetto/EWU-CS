package actbs.system.vehicle;

import actbs.system.trip.Cruise;
import actbs.system.trip.sections.CruiseSection;
import actbs.system.trip.sections.SectionType;
import actbs.system.trip.sections.TripSection;
import actbs.system.trip.sections.berths.BerthClass;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Observable;

public class Ship extends Observable {
	private final ArrayList<TripSection> sections;
	private final ArrayList<Cruise> cruises;
	private final String shipID;

	public Ship(String shipID) {
		this.shipID = shipID;
		sections = new ArrayList<>();
		cruises = new ArrayList<>();
	}

	public String getShipID() {
		return shipID;
	}

	public void createSection(SectionType sectionType, int rows, BerthClass berthClass, int price) throws ParseException {
		if (rows > 100) {
			System.out.println("Rows must not be greater than 100");
			return;
		}
		if (berthClass == BerthClass.F || berthClass == BerthClass.B || berthClass == BerthClass.E) {
			throw new ParseException("Wrong berth class", 3);
		}
		for (TripSection section : sections) {
			if (section.matchesBerthClass(berthClass)) {
				System.out.println("Could not create cruise section. Section of that type already exists on cruise. (If reading from file, this is intended)");
				return;
			}
		}
		sections.add(new CruiseSection(sectionType, rows, berthClass, price));
		setChanged();
		notifyObservers();
	}

	public ArrayList<TripSection> getSections() {
		return sections;
	}

	public ArrayList<Cruise> getCruises() {
		return cruises;
	}

	public String toString(){
		//Example output: (SS1[FA:550:W:12,DF:650:M:6,C:650:M:6,DC:1050:S:3])
		String out = shipID + "[";
		for(TripSection section :sections){
			out += section.toString() + ",";
		}
		return out.substring(0, out.length() - 1) + "]";
	}
}
