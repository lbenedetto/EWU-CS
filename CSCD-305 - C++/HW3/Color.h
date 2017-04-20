#ifndef HW3_COLOR_H
#define HW3_COLOR_H


class Color {
private:
	double r;
	double g;
	double b;
public:
	Color() : r(0), g(0), b(0) {}

	Color(double r, double g, double b) : r(r), g(g), b(b) {}

	Color(const Color &c) : r(c.r), g(c.g), b(c.b) {}

	friend bool operator==(const Color &c1, const Color &c2);

	friend Color operator+(const Color &b1, const Color &b2);

	friend Color operator-(const Color &b1, const Color &b2);

	double getRed() {
		return r;
	}

	double getGreen() {
		return g;
	}

	double getBlue() {
		return b;
	}
};


#endif //HW3_COLOR_H
