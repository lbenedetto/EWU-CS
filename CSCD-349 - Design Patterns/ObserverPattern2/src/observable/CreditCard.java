package observable;

public class CreditCard extends Account {
	public CreditCard(Double startBalance, String name, int accountNumber) {
		super(startBalance, name + "#" + accountNumber);
	}
}
