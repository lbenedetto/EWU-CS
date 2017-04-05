package hosting.basePlans;

public class Basic extends Plan {
	public Basic() {
		super("Basic Hosting");
	}

	@Override
	public double getCost() {
		return 25;
	}
}
