package hosting.addons;


import hosting.basePlans.Plan;

public class Subversion extends AddonService {

	public Subversion(Plan plan) {
		this.plan = plan;
	}

	@Override
	public String getDescription() {
		return plan.getDescription() + ", Subversion Hosting";
	}

	@Override
	public double getCost() {
		return 30 + plan.getCost();
	}
}
