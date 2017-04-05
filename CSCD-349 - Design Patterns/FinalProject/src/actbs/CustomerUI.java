package actbs;

import actbs.managers.AirportSystemManager;
import actbs.managers.SeaportSystemManager;

import static actbs.Reader.*;

class CustomerUI {

	public static void main(String[] args) {
		AirportSystemManager airportManager = new AirportSystemManager();
		SeaportSystemManager seaportManager = new SeaportSystemManager();
		int c = 0;
		while (c != 12) {
			displayMenu();
			c = readInt("choice");
			while (c < 1 || c > 12) {
				System.out.println("Invalid choice");
				c = readInt("choice");
			}
			switch (c) {
				case 1:
					airportManager.loadSystemDetails(readFilename());
					break;
				case 2:
					airportManager.displaySystemDetails();
					break;
				case 3:
					airportManager.showAvailableTrips(airportManager.findAvailableTrips(readLocation("origin"), readSingleDestinationAsArray(), readDate("departure"), readBerthClass("airport")));
					break;
				case 4:
					airportManager.bookBerth(readCompany(), readID("trip"), readBerthClass("airport"), readSeatLocation());
					break;
				case 5:
					airportManager.bookAnyBerth(readCompany(), readID("trip"), readBerthClass("airport"));
					break;
				case 6:
					airportManager.bookSeatWithPreferences(readCompany(), readID("trip"), readBerthClass("airport"), readPreference(" window"), readPreference("n aisle"));
					break;
				case 7:
					seaportManager.loadSystemDetails(readFilename());
					break;
				case 8:
					seaportManager.displaySystemDetails();
					break;
				case 9:
					seaportManager.showAvailableTrips(seaportManager.findAvailableTrips(readLocation("origin"), readDestinationArray(), readDate("departure"), readBerthClass("seaport")));
					break;
				case 10:
					seaportManager.bookBerth(readCompany(), readID("trip"), readBerthClass("seaport"), readSeatLocation());
					break;
				case 11:
					seaportManager.bookAnyBerth(readCompany(), readID("trip"), readBerthClass("seaport"));
			}
		}
	}

	private static void displayMenu() {
		System.out.print("Airport System Management\n" +
				"1. Load airport system from file.\n" +
				"2. Display details of the airport system.\n" +
				"3. Find a flight based on origin, destination, departure date and desired seat class\n" +
				"4. Book a specific seat on a specific flight.\n" +
				"5. Book any seat on a specific flight\n" +
				"6. Book a seat on a flight given only a seating preference\n" +
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
				"Seaport System Management\n" +
				"7. Load seaport system from file.\n" +
				"8. Display details of the seaport system.\n" +
				"9. Find a cruise based on origin, destination, departure date and desired cabin class\n" +
				"10. Book a specific cabin on a cruise.\n" +
				"11. Book any cabin on a specific cruise.\n" +
				"12. Exit.\n\n");
	}
}
