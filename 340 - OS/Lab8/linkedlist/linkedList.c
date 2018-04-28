#include "linkedList.h"

void deleteNode(Node *p, Node *d, Node *n) {
	if (p != NULL) p->next = n;
	if (n != NULL) n->prev = p;
	d->next = NULL;
	d->prev = NULL;
	free(d->data);
	free(d);
}

void insertNode(Node *p, Node *new, Node *n) {
	new->prev = p;
	new->next = n;
	if (p != NULL) p->next = new;
	if (n != NULL) n->prev = new;
}

/**
 * @brief The so called "constructor" for the linked list
 *
 * The linked list constructor builds a non circular linked list that
 * contains a dummy head node.  This is meant to be similar to a Java
 * linked list constructor.
 *
 * @return LinkedList * - The linked list pointer that contains a dummy head node but is not circular.
 */
LinkedList *linkedList() {
	LinkedList *ll = calloc(1, sizeof(LinkedList));
	ll->size = 0;
	ll->head = calloc(1, sizeof(Node));
	return ll;
}


/**
 * @brief The add last function for the linked list
 *
 * Appends the specified node to the end of this list
 *
 * @param theList - The specified linked list
 * @param nn - The node to be added
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 * @warning - The passed in Node * nn is checked - exit(-99) if NULL
 */
void addLast(LinkedList *theList, Node *nn) {
	if (theList == NULL || nn == NULL) exit(-99);
	if (theList->size == 0) {
		insertNode(theList->head, nn, theList->head);
	} else {
		insertNode(theList->head->prev, nn, theList->head);
	}
	theList->size++;
}


/**
 * @brief The add first function for the linked list
 *
 * Appends the specified node to the beginning of this list
 *
 * @param theList - The specified linked list
 * @param nn - The node to be added
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 * @warning - The passed in Node * nn is checked - exit(-99) if NULL
 */
void addFirst(LinkedList *theList, Node *nn) {
	if (theList == NULL || nn == NULL) exit(-99);
	if (theList->size == 0) {
		insertNode(theList->head, nn, theList->head);
	} else {
		insertNode(theList->head, nn, theList->head->next);
	}
	theList->size++;
}


/**
 * @brief The remove first function for the linked list
 *
 * Removes the specified node to the beginning of this list
 *
 * @param theList - The specified linked list
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 */
void removeFirst(LinkedList *theList) {
	if (theList == NULL) exit(-99);
	Node *h = theList->head;
	deleteNode(h, h->next, h->next->next);
	theList->size--;
}


/**
 * @brief The remove last function for the linked list
 *
 * Removes the specified node to the end of this list
 *
 * @param theList - The specified linked list
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 */
void removeLast(LinkedList *theList) {
	if (theList == NULL) exit(-99);
	Node *h = theList->head;
	deleteNode(h->prev->prev, h->prev, h);
	theList->size--;
}


/**
 * @brief The remove item function for the linked list
 *
 * Removes the first occurrence of the specified element from this list, if it
 * is present.  If this list does not contain the element, it is unchanged.
 *
 * @param theList - The specified linked list
 * @param nn - The node to be removed
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 * @warning - The passed in Node * nn is checked - exit(-99) if NULL
 */
void removeItem(LinkedList *theList, Node *nn) {
	if (theList == NULL || nn == NULL) exit(-99);
	Node *c = theList->head;
	for (int i = 0; i < theList->size; i++) {
		c = c->next;
		if (strcmp(c->data, nn->data) == 0) {
			deleteNode(c->prev, c, c->next);
			theList->size--;
			break;
		}
	}
	free(nn->data);
	free(nn);
}


/**
 * @brief Empties the list and its contents
 *
 * Removes all of the elements from this list.  The list will be empty after the function completes
 *
 * @param theList - The specified linked list
 *
 * @warning - The passed in LinkedList * theList is checked - if NULL nothing happens
 */
void clearList(LinkedList *theList) {
	if (theList == NULL) return;
	int size = theList->size;
	for (int i = 0; i < size; i++) {
		removeFirst(theList);
	}
	free(theList->head);
	theList->size = 0;
}


/**
 * @brief Prints the contents of this linked list
 *
 * Prints the contents, if there are any of this linked list.  If the list is NULL
 * or empty, "Empty List" is printed.
 *
 * @param theList - The specified linked list
 *
 * @warning - The passed in LinkedList * theList is checked - if NULL "Empty List" is printed
 */
void printList(const LinkedList *theList, FILE *fp) {
	if (theList == NULL) exit(-99);
	Node *c = theList->head;
	for (int i = 0; i < theList->size; i++) {
		c = c->next;
		fprintf(fp, "%s\n", c->data);
	}
}