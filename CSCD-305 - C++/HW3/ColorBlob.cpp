#include "ColorBlob.h"

bool operator==(const ColorBlob &b1, const ColorBlob &b2) {
	if (b1.height != b2.height || b1.width != b2.width) return false;
	for (int i = 0; i < b1.height; i++) {
		for (int j = 0; j < b1.width; j++) {
			if (b1.data[i][j] == b2.data[i][j]) return false;
		}
	}
	return true;
}

ColorBlob operator+(const ColorBlob &b1, const ColorBlob &b2) {
	int h = min(b1.height, b2.height);
	int w = min(b1.width, b2.width);
	ColorBlob blob = ColorBlob(h, w);
	for (int i = 0; i < blob.height; i++) {
		for (int j = 0; j < blob.width; j++) {
			blob.data[i][j] = b1.data[i][j] + b2.data[i][j];
		}
	}
	return blob;
}

ColorBlob operator-(const ColorBlob &b1, const ColorBlob &b2) {
	int h = min(b1.height, b2.height);
	int w = min(b1.width, b2.width);
	ColorBlob blob = ColorBlob(h, w);
	for (int i = 0; i < blob.height; i++) {
		for (int j = 0; j < blob.width; j++) {
			blob.data[i][j] = b1.data[i][j] - b2.data[i][j];
		}
	}
	return blob;
}

ColorBlob operator*(const ColorBlob &blob, const Color &color) {
	return ColorBlob();
}

bool operator!(const ColorBlob &blob) {
	return false;
}

ostream &operator<<(ostream &stream, const ColorBlob &blob) {

}

istream &operator>>(istream &stream, ColorBlob &blob) {

}

//friend Color **operator[](int i) const {
//	return data[i];
//}

int ColorBlob::getHeight() {
	return height;
}

int ColorBlob::getWidth() {
	return width;
}

Color **ColorBlob::getData() {
	return data;
}
/*
for (int i = 0; i < height; i++) {
	for (int j = 0; j < width; j++) {

	}
}
 */
