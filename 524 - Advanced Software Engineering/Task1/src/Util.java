import java.util.Arrays;

public class Util {
	public static double[] parseInts(String s) {
		return Arrays.stream(s.split(",")).mapToDouble(Double::parseDouble).toArray();
	}

}
