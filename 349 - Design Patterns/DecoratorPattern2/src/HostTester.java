import hosting.addons.FlashMedia;
import hosting.addons.MySQL;
import hosting.addons.Subversion;
import hosting.basePlans.*;

public class HostTester {
	public static void main(String[] args) {
		Plan myPlan = new Basic();
		myPlan = new Subversion(myPlan);
		myPlan = new FlashMedia(myPlan);
		myPlan = new MySQL(myPlan);
		myPlan = new MySQL(myPlan);
		myPlan = new MySQL(myPlan);
		printPlanCost(myPlan);
	}

	private static void printPlanCost(Plan plan) {
		System.out.println(plan.getDescription() + " costs $" + String.format("%.2f", plan.getCost()) + " a month");
	}
}
