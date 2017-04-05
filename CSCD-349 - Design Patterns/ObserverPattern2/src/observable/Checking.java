package observable;

public class Checking extends Account {
	public Checking(Double startBalance, int accountNumber) {
		super(startBalance, "CheckingAccount#" + accountNumber);
	}
}
