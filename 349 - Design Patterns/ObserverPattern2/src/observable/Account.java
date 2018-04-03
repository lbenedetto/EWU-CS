package observable;

import java.util.Observable;

public abstract class Account extends Observable {
	private Double balance;
	private String name;

	Account(Double startBalance, String name) {
		balance = startBalance;
		this.name = name;
	}

	public void setBalance(Double newBalance) {
		balance = newBalance;
		setChanged();
		notifyObservers(balance);
	}

	public Double getBalance() {
		return balance;
	}

	public String getName() {
		return name;
	}
}
