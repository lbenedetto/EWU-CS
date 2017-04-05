package actbs.system.trip.sections.berths;

public abstract class Berth {
	private final String ID;
	//A berth is the closest term I could describe to cover the possible extensions of this program
	//Berth: shelflike sleeping space, as on a ship, airplane, or railroad car.
	private boolean booked;

	Berth(int r, int c) {
		booked = false;
		String[] letterMap = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		ID = (r + 1) + letterMap[c];
	}

	public boolean isNotBooked() {
		return !booked;
	}

	private void setBooked() {
		this.booked = true;
	}

	public void book() {
		if (isNotBooked()) {
			System.out.println("Booked " + ID);
			setBooked();
		} else
			System.out.println("Could not book berth. Berth is already booked");
	}
}
