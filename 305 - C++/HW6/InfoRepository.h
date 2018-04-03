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
	cout << "Printing " << iName << " info.....\n\n";
	InfoNode<T, U, V> *curr = first;
	while (curr != nullptr) {
		curr->print();
		curr = curr->next;
	}
}

template<class T, class U, class V>
void InfoRepository<T, U, V>::retrieveInfoNode(T &t) {
	cout << "\nRetrieving Node with id " << t << " from the database.....\n";
	InfoNode<T, U, V> *curr = first;
	while (curr != nullptr) {
		if (curr->dataOne == t) {
			cout << "Node with id " << t << "was found in the database.\n\n";
			cout << "Here's the information .....\n";
			curr->print();
			cout << endl;
			return;
		}
		curr = curr->next;
	}
	cout << "No Node with id " << t << " exists in the list.";
}

template<class T, class U, class V>
InfoRepository<T, U, V>::~InfoRepository() {
	InfoNode<T, U, V> *curr = first;
	InfoNode<T, U, V> *prev = nullptr;
	while (curr != nullptr) {
		if (prev != nullptr) {
			delete prev;
		}
		prev = curr;
		curr = curr->next;
	}
	delete prev;
}

#endif //HW6_INFOREPOSITORY_H
