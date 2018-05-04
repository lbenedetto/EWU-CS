package task3;

public class Real {
	private int shift;
	private int value;

	public Real(int valueBits, int shiftBits, float encodeValue) {
		if (valueBits + shiftBits > 32) throw new RuntimeException("Invalid size (>32)");
		var str = String.valueOf(encodeValue);
		shift = str.indexOf('.');
		str = str.replace(".", "");
		shift = str.length() - shift;
		value = Integer.valueOf(str);
	}

	public static void main(String[] args) {
		new Real(1, 1, 3.14159f);
	}

	public Real add(Real b) {
		return null;
	}

	public Real subtract(Real b) {
		return null;
	}

	public Real multiply(Real b) {
		return null;
	}

	public Real divide(Real b) {
		return null;
	}

	public Real interpolate(Real b) {
		return null;
	}

	public int compare(Real b) {
		return 0;
	}

	public float getValue() {
		return 0f;
	}

}
