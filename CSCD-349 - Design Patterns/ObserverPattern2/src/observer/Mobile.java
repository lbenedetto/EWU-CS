package observer;

import observable.Account;

import java.util.Map;

public class Mobile extends Display {
	@Override
	public void display() {
		//Display logic, could be different for other views
		for (Map.Entry e : accounts.entrySet()) {
			System.out.println(((Account) e.getKey()).getName() + ": $" + e.getValue());
		}
	}
}
