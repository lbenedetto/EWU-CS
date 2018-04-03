package actbs.system.trip;

import actbs.system.trip.sections.SectionType;
import actbs.system.trip.sections.berths.BerthClass;

import java.text.ParseException;

interface ITrip {
	void createSection(SectionType sectionType, int rows, BerthClass berthClass, int price) throws ParseException;

	String getExtraInfo();
}
