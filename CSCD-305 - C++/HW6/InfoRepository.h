#ifndef HW6_INFOREPOSITORY_H
#define HW6_INFOREPOSITORY_H

#include <string>
#include "InfoNode.h"

template<class T, class U, class V>
class InfoRepository {
public:
	InfoRepository(std::string name) : first(0), iName(name) {}

	~InfoRepository();

	void addInfo(T t, U u, V v);

	bool isEmpty();

	void printInformation();

	void retrieveInfoNode(T &t);

private:
	InfoNode<T, U, V> *first;
	std::string iName;
};

template<class T, class U, class V>
void InfoRepository<T, U, V>::addInfo(T t, U u, V v) {
	InfoNode<T, U, V> *n = new InfoNode<T, U, V>(t, u, v, first);
	first = n;
}

template<class T, class U, class V>
bool InfoRepository<T, U, V>::isEmpty() {
	return first == nullptr;
}

template<class T, class U, class V>
void InfoRepository<T, U, V>::printInformation() {
	InfoNode<T, U, V> *curr = first;
	while (curr != nullptr) {
		cout << curr;
		curr = curr->next;
	}
}

template<class T, class U, class V>
void InfoRepository<T, U, V>::retrieveInfoNode(T &t) {
	InfoNode<T, U, V> *curr = first;
	while (curr != nullptr) {
		if (curr->dataOne == t) {
			cout << curr;
			return;
		}
		curr = curr->next;
	}
	cout << "Specified InfoNode does not exist in the Info Repository";
}

template<class T, class U, class V>
InfoRepository<T, U, V>::~InfoRepository() {

}

#endif //HW6_INFOREPOSITORY_H
