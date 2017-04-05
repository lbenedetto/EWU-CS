package actbs.system.trip.sections.berths;

public class Seat extends Berth {

	private final boolean isAisleSeat;
	private final boolean isWindowSeat;

	public Seat(boolean isWindowSeat, boolean isAisleSeat, int r, int c) {
		super(r, c);
		this.isAisleSeat = isAisleSeat;
		this.isWindowSeat = isWindowSeat;
	}

	public boolean isAisleSeat() {
		return isAisleSeat;
	}

	public boolean isWindowSeat() {
		return isWindowSeat;
	}
}
