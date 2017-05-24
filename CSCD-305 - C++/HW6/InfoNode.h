#ifndef HW6_INFONODE_H
#define HW6_INFONODE_H


#include <ostream>
#include <iostream>
#include <iomanip>
#include <sstream>

using namespace std;

template<class T, class U, class V>
class InfoRepository;

template<class T, class U, class V>
class InfoNode {
	//template<class W, class X, class Y> friend class InfoRepository;
	friend class InfoRepository<T, U, V>;

public:
	InfoNode(T &t, U &u, V &v, InfoNode<T, U, V> *p) : dataOne(t), dataTwo(u), dataThree(v), next(p) {}

	void print();

private:
	T dataOne;
	U dataTwo;
	V dataThree;
	InfoNode *next;

};

template<class T, class U, class V>
void InfoNode<T, U, V>::print() {
	const int width = 20;
	const char separator = ' ';
	cout << left << setw(width) << setfill(separator) << dataOne;
	cout << left << setw(width) << setfill(separator) << dataTwo;
	cout << left << setw(width) << setfill(separator) << dataThree;
	cout << endl;
}

#endif //HW6_INFONODE_H
