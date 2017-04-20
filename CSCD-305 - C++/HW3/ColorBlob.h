#ifndef HW3_COLORBLOB_H
#define HW3_COLORBLOB_H

#include <iostream>

#include "Color.h"

using namespace std;

class ColorBlob {
private:
	int width;
	int height;
	Color **data;
public:
	ColorBlob() : width(0), height(0), data() {}

	ColorBlob(int width, int height) : width(width), height(height) {
		data = new Color *[height];
		for (int i = 0; i < height; i++) {
			data[i] = new Color[width];
		}
	}

	ColorBlob(int width, int height, Color color) : width(width), height(height) {
		data = new Color *[height];
		for (int i = 0; i < height; i++) {
			data[i] = new Color[width];
			for (int j = 0; j < width; j++) {
				data[i][j] = color;
			}
		}
	}

	friend bool operator==(const ColorBlob &b1, const ColorBlob &b2);

	friend ColorBlob operator+(const ColorBlob &b1, const ColorBlob &b2);

	friend ColorBlob operator-(const ColorBlob &b1, const ColorBlob &b2);

	friend ColorBlob operator*(const ColorBlob &blob, const Color &color);

	friend bool operator!(const ColorBlob &blob);

	friend ostream &operator<<(ostream &stream, const ColorBlob &blob);

	friend istream &operator>>(istream &stream, ColorBlob &blob);

	int getHeight();

	int getWidth();

	Color **getData();
};


#endif //HW3_COLORBLOB_H
