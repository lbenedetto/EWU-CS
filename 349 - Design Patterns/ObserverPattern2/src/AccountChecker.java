import observer.*;
import observable.*;

public class AccountChecker {
	public static void main(String[] args) {
		Display d = new Desktop();
		Checking c = new Checking(165.50, 5466234);
		Savings s = new Savings(485.20, 5645624);
		CreditCard cc1 = new CreditCard(234.34, "Amex", 1231);
		CreditCard cc2 = new CreditCard(454.34, "Visa", 3422);
		d.addAccount(c);
		d.addAccount(s);
		d.addAccount(cc1);
		d.addAccount(cc2);
		d.display();
		c.setBalance(234.50);
	}//end main
}//end class