package actbs.system.trip.sections;

import actbs.system.trip.sections.berths.Berth;
import actbs.system.trip.sections.berths.BerthClass;

public abstract class TripSection {
	final Berth[][] berths;
	private final BerthClass berthClass;
	private final SectionType sectionType;
	private int berthPrice;

	TripSection(int rows, int cols, int price, BerthClass berthClass, SectionType sectionType) {
		berths = new Berth[rows][cols];
		berthPrice = price;
		this.berthClass = berthClass;
		this.sectionType = sectionType;
	}

	public void overridePrice(int price) {
		berthPrice = price;
	}

	public void bookAnyBerth() {
		for (Berth[] row : berths) {
			for (Berth berth : row) {
				if (berth.isNotBooked()) {
					berth.book();
					return;
				}
			}
		}
		System.out.println("Could not bookBerth any seat, section is fully booked");
	}

	public boolean hasAvailableBerths() {
		for (Berth[] row : berths)
			for (Berth seat : row)
				if (seat.isNotBooked())
					return true;
		return false;
	}

	public void bookBerth(int row, int col) {
		if (row > berths.length || col > berths[0].length) {
			System.out.println("Invalid seat ID");
		} else {
			berths[row - 1][col - 1].book();
		}
	}

	public boolean matchesBerthClass(BerthClass berthClass) {
		return this.berthClass.equals(berthClass);
	}

	public int[] toDetailString() {
		int[] booked = {0,0};
		for (Berth[] b1 : berths) {
			for (Berth b : b1) {
				if (b.isNotBooked()) booked[0]++;
				else booked[1]++;
			}
		}
		return booked;
	}

	public String toString() {
		return berthClass.toString() + ":" + berthPrice + ":" + sectionType.toString() + ":" + berths.length;
	}
}
