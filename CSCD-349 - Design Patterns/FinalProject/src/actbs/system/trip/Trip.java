package actbs.system.trip;


import actbs.system.location.Location;
import actbs.system.trip.sections.TripSection;
import actbs.system.trip.sections.berths.BerthClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public abstract class Trip implements ITrip {
	private final Location orig;
	private final ArrayList<Location> destinations;
	private final Date departureDate;
	private final Date arrivalDate;
	private final String tripID;
	ArrayList<TripSection> sections;

	Trip(Location orig, ArrayList<Location> dest, String tripID, Date departureDate, Date arrivalDate) {
		this(orig, dest, tripID, departureDate, arrivalDate, new ArrayList<>());
	}

	Trip(Location orig, ArrayList<Location> dest, String tripID, Date departureDate, Date arrivalDate, ArrayList<TripSection> sections) {
		destinations = dest;
		this.orig = orig;
		this.departureDate = departureDate;
		this.arrivalDate = arrivalDate;
		this.tripID = tripID;

		this.sections = sections;
	}

	public Location getOrigin() {
		return orig;
	}

	public ArrayList<String> getDestinationsAsStrings() {
		ArrayList<String> temp = new ArrayList<>();
		for (Location location : destinations) {
			temp.add(location.getName());
		}
		Collections.sort(temp);
		return temp;
	}

	public boolean departsOn(Date date) {
		Calendar thisDate = Calendar.getInstance();
		thisDate.setTime(this.departureDate);
		Calendar thatDate = Calendar.getInstance();
		thatDate.setTime(date);
		return thisDate.get(Calendar.YEAR) == thatDate.get(Calendar.YEAR) && thisDate.get(Calendar.MONTH) == thatDate.get(Calendar.MONTH) && thisDate.get(Calendar.DATE) == thatDate.get(Calendar.DATE);
	}

	public void overridePrice(BerthClass berthClass, int price) {
		for (TripSection section : sections) {
			if (section.matchesBerthClass(berthClass)) {
				section.overridePrice(price);
			}
		}
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	private String getArrivalDateString() {
		return getDateString(arrivalDate);
	}

	private String getDepartureDateString() {
		return getDateString(departureDate);
	}

	private String getDateString(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR) + "," + (cal.get(Calendar.MONTH) + 1) + "," + cal.get(Calendar.DATE) + "," + cal.get(Calendar.HOUR_OF_DAY) + "," + cal.get(Calendar.MINUTE);
	}

	private String getDestinationsString() {
		String out = "";
		for (Location location : destinations) {
			out += location.getName() + ",";
		}
		return out.substring(0, out.length() - 1);
	}

	String getSectionsString() {
		String out = "[";
		if (sections.isEmpty()) {
			return out + "]";
		}
		for (TripSection section : sections) {
			out += section.toString() + ",";
		}
		return out.substring(0, out.length() - 1) + "]";
	}

	private String getTripID() {
		return tripID;
	}

	public boolean hasAvailableBerths() {
		for (TripSection section : sections) {
			if (!section.hasAvailableBerths()) {
				return false;
			}
		}
		return true;
	}

	public boolean hasAvailableBerths(BerthClass seatClass) {
		for (TripSection section : sections) {
			if (section.matchesBerthClass(seatClass)) {
				if (!section.hasAvailableBerths()) {
					return false;
				}
			}
		}
		return true;
	}

	public void bookBerth(BerthClass berthClass, int row, int col) {
		for (TripSection section : sections) {
			if (section.matchesBerthClass(berthClass)) {
				section.bookBerth(row, col);
				return;
			}
		}
		System.out.println("Could not book berth, there were no sections of the requested class");
	}

	public void bookAnyBerth(BerthClass berthClass) {
		for (TripSection section : sections) {
			if (section.matchesBerthClass(berthClass)) {
				section.bookAnyBerth();
				return;
			}
		}
		System.out.println("Could not book berth, there were no sections of the requested class");
	}

	public String toString(boolean detailMode) {
		String out = getTripID() + "|" + getDepartureDateString() + "|" + getArrivalDateString() + "|" + getOrigin().getName() + "|" + getDestinationsString() + getExtraInfo();
		if (detailMode) {
			int[] booked = {0, 0};
			for (TripSection section : sections) {
				int[] temp = section.toDetailString();
				booked[0] += temp[0];
				booked[1] += temp[1];
			}
			return out + " " + booked[0] + " not booked and " + booked[1] + " booked\n";
		} else {
			return out;
		}
	}
	public String toString(){
		return toString(false);
	}
}
