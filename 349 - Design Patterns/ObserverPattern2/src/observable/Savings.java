package observable;

public class Savings extends Account {
	public Savings(Double startBalance, int accountNumber) {
		super(startBalance, "CheckingAccount#" + accountNumber);
	}
}
