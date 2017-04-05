package observer;

import observable.Account;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public abstract class Display implements Observer {
	HashMap<Account, Double> accounts;

	Display() {
		accounts = new HashMap<>();
	}

	public void addAccount(Account account) {
		accounts.put(account, account.getBalance());
		account.addObserver(this);
	}

	public abstract void display();

	@Override
	public void update(Observable o, Object arg) {
		Double newBalance = (Double) arg;
		Account account = (Account) o;
		accounts.put(account, newBalance);
		display();
	}
}
