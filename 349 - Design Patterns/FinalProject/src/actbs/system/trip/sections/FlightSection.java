package actbs.system.trip.sections;

import actbs.system.trip.sections.berths.Berth;
import actbs.system.trip.sections.berths.BerthClass;
import actbs.system.trip.sections.berths.Seat;

public class FlightSection extends TripSection {
	public FlightSection(SectionType sectionType, int rows, BerthClass berthClass, int price) {
		super(rows, sectionType.getCols(), price, berthClass, sectionType);

		int cols = sectionType.getCols();
		for (int c = 0; c < cols; c++) {
			boolean isWindowSeat = (c == (cols - 1) || c == 0);
			boolean isAisleSeat = sectionType.isAisleSeat(c);
			for (int r = 0; r < rows; r++)
				berths[r][c] = new Seat(isWindowSeat, isAisleSeat, r, c);
		}
	}

	public void bookSeatWithPreferences(boolean preferWindow, boolean preferAisle) {
		for (Berth[] row : berths) {
			for (Berth berth : row) {
				Seat seat = (Seat) berth;
				if (seat.isWindowSeat() == preferWindow && seat.isAisleSeat() == preferAisle && seat.isNotBooked()) {
					seat.book();
					return;
				}
			}
		}
		System.out.println("Could not find a seat matching preferences");
		bookAnyBerth();
	}
}
