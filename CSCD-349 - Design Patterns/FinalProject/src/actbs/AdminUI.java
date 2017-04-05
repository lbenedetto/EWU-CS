package actbs;

import actbs.managers.AirportSystemManager;
import actbs.managers.SeaportSystemManager;

import java.text.ParseException;

import static actbs.Reader.*;

class AdminUI {
	public static void main(String[] args) {
		AirportSystemManager airportManager = new AirportSystemManager();
		SeaportSystemManager seaportManager = new SeaportSystemManager();
		int c = 0;
		while (c != 18) {
			displayMenu();
			c = readInt("choice");
			while (c < 1 || c > 18) {
				System.out.println("Invalid choice");
				c = readInt("choice");
			}
			switch (c) {
				case 1:
					airportManager.loadSystemDetails(readFilename());
					break;
				case 2:
					airportManager.saveSystemDetails(readFilename());
					break;
				case 3:
					airportManager.displaySystemDetails();
					break;
				case 4:
					airportManager.displayDetailedSystemReport();
					break;
				case 5:
					airportManager.createLocation(readLocation("airport"));
					break;
				case 6:
					airportManager.createCompany(readCompany());
					break;
				case 7:
					airportManager.createTrip(readCompany(), readID("trip"), readDate("departure"), readDate("arrival"), readLocation("origin"), readSingleDestinationAsArray());
					break;
				case 8:
					try {
						airportManager.createSection(readCompany(), readID("trip"), readBerthClass("airport"), readSectionType(), readInt("rows"), readInt("price"));
					} catch (ParseException e) {
						System.out.println("Invalid berth class");
					}
					break;
				case 9:
					seaportManager.loadSystemDetails(readFilename());
					break;
				case 10:
					seaportManager.saveSystemDetails(readFilename());
					break;
				case 11:
					seaportManager.displaySystemDetails();
					break;
				case 12:
					seaportManager.displayDetailedSystemReport();
					break;
				case 13:
					seaportManager.createLocation(readLocation("seaport name"));
					break;
				case 14:
					seaportManager.createCompany(readCompany());
					break;
				case 15:
					seaportManager.createTrip(readCompany(), readID("trip"), readDate("departure"), readDate("arrival"), readLocation("origin seaport"), readDestinationArray());
					break;
				case 16:
					try {
						seaportManager.createSection(readCompany(), readID("trip"), readBerthClass("seaport"), readSectionType(), readInt("rows"), readInt("price"));
					} catch (ParseException e) {
						System.out.println("Invalid berth class");
					}
					break;
				case 17:
					seaportManager.createShip(readCompany(), readID("ship"));
			}
		}
	}

	private static void displayMenu() {
		System.out.print("Airport System Management\n" +
				"1. Load airport system from file.\n" +
				"2. Save airport system to file.\n" +
				"3. Display details of the airport system.\n" +
				"4. Display detailed report of current state of airport system.\n" +
				"5. Create airport.\n" +
				"6. Create airline.\n" +
				"7. Create flight.\n" +
				"8. Create flight section.\n" +
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
				"Seaport System Management\n" +
				"9. Load seaport system from file.\n" +
				"10. Save airport system to file.\n" +
				"11. Display details of the seaport system.\n" +
				"12. Display detailed report of current state of seaport system.\n" +
				"13. Create seaport.\n" +
				"14. Create cruiseline.\n" +
				"15. Create cruise.\n" +
				"16. Create cruise section.\n" +
				"17. Create ship.\n" +
				"18. Exit.\n\n");
	}
}
