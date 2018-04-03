import java.math.BigInteger;
import java.util.HashMap;
import java.util.Stack;

class Parser {
	/**
	 * Evaluates a postfix expression
	 *
	 * @param valueOf HashMap of letters to their BigInteger values
	 * @param s       String - postfix expression to evaluate
	 * @return BigInteger
	 */
	public static BigInteger evaluate(HashMap<Character, BigInteger> valueOf, String s) {
		char curr;
		Stack<BigInteger> stack = new Stack<>();
		for (int i = 0; i < s.length(); i++) {
			curr = s.charAt(i);
			if (isLetter(curr))
				stack.push(valueOf.get(curr));
			else
				stack.push(execute(stack.pop(), curr, stack.pop()));
		}
		return stack.pop();
	}

	/**
	 * Convert an infix expression to a postfix expression
	 *
	 * @param s String - infix expression
	 * @return String - postfix expression
	 */
	public static String parse(String s) {
		CharStack stack = new CharStack();
		String postfix = "";
		char curr;
		for (int i = 0; i < s.length(); i++) {
			curr = s.charAt(i);
			if (isLetter(curr))
				postfix += curr;
			else if (curr == '(')
				stack.push(curr);
			else if (curr == ')') {
				while (stack.peek() != '(')
					postfix += stack.pop();
				stack.pop();
			} else {
				while (stack.isNotEmpty() && precedenceStack(stack.peek()) > precedenceCurr(curr))
					postfix += stack.pop();
				stack.push(curr);
			}
		}
		while (stack.isNotEmpty())
			postfix += stack.pop();
		return postfix;
	}

	/**
	 * Determine precedence for an operand in the stack
	 *
	 * @param c char
	 * @return int - precedence
	 */
	private static int precedenceStack(char c) {
		switch (c) {
			case '+':
			case '-':
				return 2;
			case '*':
			case '/':
				return 4;
			case '^':
				return 6;
			default:
				return 0;
		}
	}

	/**
	 * Determine precedence of the current char
	 *
	 * @param c char
	 * @return int - precedence
	 */
	private static int precedenceCurr(char c) {
		switch (c) {
			case '+':
			case '-':
				return 1;
			case '*':
			case '/':
				return 3;
			case '^':
				return 5;
			default:
				return 100;
		}
	}

	/**
	 * Check if a char is a letter
	 *
	 * @param c char
	 * @return boolean
	 */
	public static boolean isLetter(char c) {
		return c >= 'A' && c <= 'Z';
	}

	/**
	 * Execute a given operation
	 *
	 * @param right     operand
	 * @param operation char
	 * @param left      operand
	 * @return BigInteger result
	 */
	private static BigInteger execute(BigInteger right, char operation, BigInteger left) {
		switch (operation) {
			case '*':
				return left.multiply(right);
			case '/':
				try {
					return left.divide(right);
				}catch(ArithmeticException e){
					System.out.println("\nTried to divide by zero");
				}
			case '+':
				return left.add(right);
			case '-':
				return left.subtract(right);
			case '^':
				return pow(left, right);
		}
		return left;
	}

	/**
	 * Safely handle raising base^exponent even if exponent is bigger than Integer.MAX_VALUE
	 *
	 * @param base     BigInteger
	 * @param exponent BigInteger
	 * @return BigInteger
	 */
	private static BigInteger pow(BigInteger base, BigInteger exponent) {
		BigInteger result = BigInteger.ONE;
		ProgressBar progressBar = new ProgressBar();
		Thread t = new Thread(progressBar);
		t.start();
		while (exponent.signum() > 0) {
			if (exponent.testBit(0)) result = result.multiply(base);
			base = base.multiply(base);
			exponent = exponent.shiftRight(1);
		}
		progressBar.stop();
		t.interrupt();
		String out = progressBar.didTakeAWhile() ? "\nDone. It might take a while to print the result\n" : "";
		System.out.print(out);
		return result;
	}
}