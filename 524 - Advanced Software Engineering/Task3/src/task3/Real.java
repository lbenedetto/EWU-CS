package task3;

public class Real {
	private int shift;
	private int value;
	private int valueBits;
	private int shiftBits;
	private int maxVal;

	public Real(int valueBits, int shiftBits, float encodeValue) {
		if (valueBits + shiftBits > 32) throw new RuntimeException("Invalid size (>32)");
		var str = String.valueOf(encodeValue);
		shift = str.indexOf('.');
		if (shift == -1)
			shift = str.length();
		str = str.replace(".", "");
		value = Integer.valueOf(str);
		this.valueBits = valueBits;
		this.shiftBits = shiftBits;
		this.maxVal = (int) Math.pow(2, valueBits) - 1;
		if (value > maxVal)
			throw new RuntimeException("Size exceeds max size capable of being stored in this representation in this configuration");
	}

	public Real add(Real b) {
		if (hasDifferentConfiguration(b))
			throw new RuntimeException("Cannot add values of different value/shift bit sizes");
		return new Real(valueBits, shiftBits, getValue() + b.getValue());
	}

	public Real subtract(Real b) {
		if (hasDifferentConfiguration(b))
			throw new RuntimeException("Cannot subtract values of different value/shift bit sizes");
		return new Real(valueBits, shiftBits, getValue() - b.getValue());
	}

	public Real multiply(Real b) {
		if (hasDifferentConfiguration(b))
			throw new RuntimeException("Cannot multiply values of different value/shift bit sizes");
		return new Real(valueBits, shiftBits, getValue() * b.getValue());
	}

	public Real divide(Real b) {
		if (hasDifferentConfiguration(b))
			throw new RuntimeException("Cannot divide values of different value/shift bit sizes");
		return new Real(valueBits, shiftBits, getValue() / b.getValue());
	}

	public Real interpolate(Real b, Real percent) {
		if (hasDifferentConfiguration(b))
			throw new RuntimeException("Cannot interpolate values of different value/shift bit sizes");
		return subtract(b).multiply(percent).add(this);
	}

	public int compare(Real b) {
		if (hasDifferentConfiguration(b))
			throw new RuntimeException("Cannot compare values of different value/shift bit sizes");
		return Float.compare(getValue(), b.getValue());
	}

	private boolean hasDifferentConfiguration(Real b) {
		return valueBits != b.valueBits || shiftBits != b.shiftBits;
	}

	public float getValue() {
		var str = String.valueOf(value);
		str = String.format("%s.%s", str.substring(0, shift), str.substring(shift));
		var fl = Float.valueOf(str);
		if (fl > maxVal)
			throw new RuntimeException("Size exceeds max size capable of being stored in this representation in this configuration");
		return fl;
	}

}
