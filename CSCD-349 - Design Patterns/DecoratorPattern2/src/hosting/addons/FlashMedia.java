package hosting.addons;


import hosting.basePlans.Plan;

public class FlashMedia extends AddonService {

	public FlashMedia(Plan plan) {
		this.plan = plan;
	}

	@Override
	public String getDescription() {
		return plan.getDescription() + ", Flash Media Server Hosting";
	}

	@Override
	public double getCost() {
		return 20 + plan.getCost();
	}
}
