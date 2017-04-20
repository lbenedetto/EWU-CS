#ifndef HW3_COLOR_H
#define HW3_COLOR_H

#include <iostream>

using namespace std;

class Color {
private:
	double r;
	double g;
	double b;
public:
	Color() : r(0), g(0), b(0) {}

	Color(double r_, double g_, double b_) {
		r = min(max(r_, 0.0), 1.0);
		g = min(max(g_, 0.0), 1.0);
		b = min(max(b_, 0.0), 1.0);
	}

	Color(const Color &c) : r(c.r), g(c.g), b(c.b) {}

	friend bool operator==(const Color &c1, const Color &c2);

	friend bool operator!=(const Color &c1, const Color &c2);

	friend Color operator+(const Color &b1, const Color &b2);

	friend Color operator-(const Color &b1, const Color &b2);

	friend Color operator*(const Color &c1, const Color &c2);

	friend ostream &operator<<(ostream &stream, const Color &c);

	friend istream &operator>>(istream &stream, Color &c);

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
