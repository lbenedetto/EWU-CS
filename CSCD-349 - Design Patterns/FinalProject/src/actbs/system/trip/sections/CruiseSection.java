package actbs.system.trip.sections;

import actbs.system.trip.sections.berths.BerthClass;
import actbs.system.trip.sections.berths.Cabin;

public class CruiseSection extends TripSection {
	public CruiseSection(SectionType sectionType, int rows, BerthClass berthClass, int price) {
		super(rows, sectionType.getCols(), price, berthClass, sectionType);
		int cols = sectionType.getCols();
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r < rows; r++)
				berths[r][c] = new Cabin(r, c);
		}
	}
}
