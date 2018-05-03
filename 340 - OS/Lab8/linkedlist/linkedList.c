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

LinkedList *linkedList() {
	LinkedList *ll = calloc(1, sizeof(LinkedList));
	ll->size = 0;
	ll->head = calloc(1, sizeof(Node));
	return ll;
}

void addLast(LinkedList *theList, Node *nn) {
	if (theList == NULL || nn == NULL) exit(-99);
	if (theList->size == 0) {
		insertNode(theList->head, nn, theList->head);
	} else {
		insertNode(theList->head->prev, nn, theList->head);
	}
	theList->size++;
}

void addFirst(LinkedList *theList, Node *nn) {
	if (theList == NULL || nn == NULL) exit(-99);
	if (theList->size == 0) {
		insertNode(theList->head, nn, theList->head);
	} else {
		insertNode(theList->head, nn, theList->head->next);
	}
	theList->size++;
}

void removeFirst(LinkedList *theList) {
	if (theList == NULL) exit(-99);
	Node *h = theList->head;
	deleteNode(h, h->next, h->next->next);
	theList->size--;
}

char *getNth(LinkedList *theList, int n) {
	if (theList == NULL || theList->size < n || n < 0) exit(-99);
	Node *curr = theList->head->next;
	for (int i = 0; i < n; i++) {
		curr = curr->next;
	}
	return curr->data;
}

char *getLast(LinkedList *theList) {
	if (theList->head->prev != NULL)
		return theList->head->prev->data;
	return "";
}

void clearList(LinkedList *theList) {
	if (theList == NULL) return;
	int size = theList->size;
	for (int i = 0; i < size; i++) {
		removeFirst(theList);
	}
	free(theList->head);
	theList->size = 0;
}

void fprintList(char *prefix, const LinkedList *theList, FILE *fp) {
	if (theList == NULL) exit(-99);
	Node *c = theList->head;
	for (int i = 0; i < theList->size; i++) {
		c = c->next;
		fprintf(fp, "%s%s\n", prefix, c->data);
	}
}

void printList(const LinkedList *theList) {
	if (theList == NULL) exit(-99);
	Node *c = theList->head;
	for (int i = 0; i < theList->size; i++) {
		c = c->next;
		printf("%s\n", c->data);
	}
}

void printLastN(char *prefix, const LinkedList *theList, int n) {
	if (theList == NULL) exit(-99);
	int startIX = theList->size - n;
	if (startIX < 0) startIX = 0;

	Node *c = theList->head;
	int i;
	for (i = 0; i < startIX; i++) {
		c = c->next;
	}
	for (i; i < theList->size - 1; i++) {
		c = c->next;
		printf("%s%d %s\n", prefix, i, c->data);
	}
}