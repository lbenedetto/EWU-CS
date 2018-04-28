#ifndef LINKEDLIST_H
#define LINKEDLIST_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct node {
	char *data;
	struct node *next;
	struct node *prev;
};
typedef struct node Node;

struct linkedlist {
	Node *head;
	int size;
};
typedef struct linkedlist LinkedList;

LinkedList *linkedList();

void addLast(LinkedList *theList, Node *nn);

void addFirst(LinkedList *theList, Node *nn);

void removeFirst(LinkedList *theList);

void removeLast(LinkedList *theList);

void removeItem(LinkedList *theList, Node *nn);

void clearList(LinkedList *theList);

void printList(const LinkedList *theList, FILE *fp);

#endif // LINKEDLIST_H
