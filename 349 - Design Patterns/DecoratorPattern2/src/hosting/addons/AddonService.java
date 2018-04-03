package hosting.addons;

import hosting.basePlans.Plan;

abstract class AddonService extends Plan {
	Plan plan;

	public abstract String getDescription();
}
