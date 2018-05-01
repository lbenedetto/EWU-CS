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

char *getNth(LinkedList *theList, int n);

char *getLast(LinkedList *theList);

void clearList(LinkedList *theList);

void fprintList(char *prefix, const LinkedList *theList, FILE *fp);

void printLastN(char *prefix, const LinkedList *theList, int n);

void deleteNode(Node *p, Node *d, Node *n);

#endif // LINKEDLIST_H
