#include <iostream>
#include "Color.h"

using namespace std;

bool operator==(const Color &c1, const Color &c2) {
	return c1.r == c2.r && c1.g == c2.g && c1.b == c2.b;
}

Color operator+(const Color &b1, const Color &b2) {
	double red = min(b1.r + b2.r, 1.0);
	double green = min(b1.g + b2.g, 1.0);
	double blue = min(b1.b + b2.b, 1.0);
	return Color(red, green, blue);
}

Color operator-(const Color &b1, const Color &b2) {
	double red = max(b1.r - b2.r, 0.0);
	double green = max(b1.g - b2.g, 0.0);
	double blue = max(b1.b - b2.b, 0.0);
	return Color(red, green, blue);
}
