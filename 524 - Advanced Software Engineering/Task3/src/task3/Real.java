package task3;

public class Real {
	private int shift;
	private int value;
	private int valueBits;
	private int shiftBits;
	private int maxVal;
	private int maxShift;
	private boolean isNegative;

	public Real(int valueBits, int shiftBits, float encodeValue) {
		if (valueBits + shiftBits > 32) throw new RuntimeException("Invalid size (>32)");
		this.valueBits = valueBits;
		this.shiftBits = shiftBits;
		this.maxVal = (int) Math.pow(2, valueBits) - 1;
		this.maxShift = (int) Math.pow(2, shiftBits) - 1;

		if (encodeValue < 0) {
			encodeValue = encodeValue * -1f;
			isNegative = true;
		}
		if (encodeValue > maxVal) encodeValue = maxVal;
		while (encodeValue != (int) encodeValue) {
			encodeValue *= 10d;
			shift++;
		}
		while (encodeValue > maxVal) {
			encodeValue /= 10d;
			encodeValue = (int) encodeValue;
			shift--;

		}
		value = (int) encodeValue;
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
		float temp = value;
		int i = 0;
		while (i < shift) {
			temp /= 10f;
			i++;
		}
		return isNegative ? temp * -1 : temp;
	}
}
