package actbs.system.trip.sections;

public enum SectionType {
	S(3, new int[]{1, 2}), M(4, new int[]{2, 3}), W(10, new int[]{3, 4, 7, 8});

	private final int cols;
	private final int[] aisleSeats;

	SectionType(int cols, int[] aisleSeats) {
		this.cols = cols;
		this.aisleSeats = aisleSeats;
	}

	public boolean isAisleSeat(int column) {
		column++;
		for (int i : aisleSeats) {
			if (i == column)
				return true;
		}
		return false;
	}

	public int getCols() {
		return cols;
	}
}
