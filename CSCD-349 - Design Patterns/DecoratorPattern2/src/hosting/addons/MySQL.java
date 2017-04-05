package hosting.addons;


import hosting.basePlans.Plan;

public class MySQL extends AddonService {

	public MySQL(Plan plan) {
		this.plan = plan;
	}

	@Override
	public String getDescription() {
		return plan.getDescription() + ", MySQL Hosting";
	}

	@Override
	public double getCost() {
		return 10 + plan.getCost();
	}
}
