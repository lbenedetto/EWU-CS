package hosting.basePlans;


public abstract class Plan {
	private String description;

	Plan(String type) {
		description = type + " plan with";
	}

	public Plan() {

	}

	public String getDescription() {
		return description;
	}

	public abstract double getCost();
}
