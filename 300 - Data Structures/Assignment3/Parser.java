import java.util.regex.Pattern;

/**
 * Extra credit attempted
 * In hindsight, this may have been easier if I had done it closer to the way explained in class
 * <expression> = <term> | <term>+<term> | <term>-<term>
 * <term>       = <factor> | <factor>*<factor> | <factor>/<factor>
 * <factor>     = <letter> | (<expression>)
 * <letter>     = A | B | C | D | ... | Z
 */
public class Parser {
	public static boolean parse(String s) {
		try {
			//Clean it up a little, save time handling problems down the line
			if (s.matches(".*?[^A-Z\\(\\)*/+-].*?")) {
				System.out.println("Input contains invalid characters: ");
				return false;
			}
			if (noRepeats(s)) {
				//Begin parsing
				s = parseLetters(s);
				if (s.equals("<expression>")) {
					System.out.println("Input successfully resolved: " + s);
					return true;
				} else {
					System.out.println("Input resolved to the following invalid output: " + s);
					return false;
				}
			}
			System.out.println("Repeat operators found by noRepeats()");
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}

	private static String parseExpression(String s) {
		return s.replaceAll("<term>\\+<term>", "<expression>").replaceAll("<term>-<term>", "<expression>").replaceAll("<term>", "<expression>");
	}

	private static String parseTerms(String s) {
		s = s.replaceAll("<factor>/<factor>", "<term>").replaceAll("<factor>\\*<factor>", "<term>").replaceAll("<factor>", "<term>");
		return parseExpression(s);
	}

	private static String parseFactors(String s) {
		if (s.contains("(") && s.contains(")")) {
			String temp = getDeepestParentheses(s);
			s = s.replace("(" + temp + ")", parseFactors(temp));
			s = parseFactors(s);
		}
		s = s.replaceAll("<letter>", "<factor>").replaceAll("<expression>", "<factor>");
		return parseTerms(s);
	}

	private static String parseLetters(String s) {
		s = s.replaceAll("[A-Z]", "<letter>");
		return parseFactors(s);
	}

	private static String getDeepestParentheses(String s) {
		s = s.substring(s.indexOf("(") + 1, findClosingParenthese(s, 1, s.indexOf("(") + 1) - 1);
		if (s.contains("(") || s.contains(")")) s = getDeepestParentheses(s);
		return s;
	}

	private static int findClosingParenthese(String s, int p, int ix) {
		int out = ix;
		if (p != 0)
			if (s.charAt(ix) == ')')
				out = findClosingParenthese(s, p - 1, ix + 1);
			else if (s.charAt(ix) == '(')
				out = findClosingParenthese(s, p + 1, ix + 1);
			else
				out = findClosingParenthese(s, p, ix + 1);
		return out;
	}

	private static boolean noRepeats(String s) {
		return !(Pattern.compile("[+|-][A-Z][+|-]").matcher(s).find() || Pattern.compile("[*|/][A-Z][*|/]").matcher(s).find());
	}
}