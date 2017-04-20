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
	ColorBlob() : ColorBlob(1, 1, {0, 0, 0}) {}

	ColorBlob(int width, int height) : ColorBlob(width, height, {0, 0, 0}) {}

	ColorBlob(int width_, int height_, Color color) {
		width = min(max(width_, 0), 1);
		height = min(max(height_, 0), 1);
		data = new Color *[height];
		for (int i = 0; i < height; i++) {
			data[i] = new Color[width];
			for (int j = 0; j < width; j++) {
				data[i][j] = color;
			}
		}
	}

	ColorBlob(const ColorBlob &blob) : width(blob.width), height(blob.height) {
		data = new Color *[height];
		for (int i = 0; i < height; i++) {
			data[i] = new Color[width];
			for (int j = 0; j < width; j++) {
				data[i][j] = blob.data[i][j];
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

	Color &operator[](const int i);

	int getHeight();

	int getWidth();

	Color **getData();
};


#endif //HW3_COLORBLOB_H
