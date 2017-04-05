package actbs.system.location;

public abstract class Location {
	private final String name;

	Location(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
