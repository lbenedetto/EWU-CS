/**
 * @file linkedList.h
 * The basic doubly linked list that will be used during the course
 * of the quarter.  This linked list is written in a very generic fashion
 * where the appropriate function pointer for the specific type is passed
 * to the functions.
 *
 * @note This file will never be changed
 */

#ifndef LINKEDLIST_H
#define LINKEDLIST_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/**
 * @brief The node structure.
 *
 * The node structure for the doubly linked list
 *
 * @note I prefer named structures and then the typedef after the structure
 */
struct node
{
    void * data;
    struct node * next;
    struct node * prev;
};
typedef struct node Node;


/**
 * @brief The linked list structure.
 *
 * The linked list structure is an attempt to emulate what we know from
 * how we did things in java.
 *
 * @note I prefer named structures and then the typedef after the structure
 * @note In true C fashion the structures will not contain function prototypes
 * or function pointer prototypes.
 */
struct linkedlist
{
    Node * head;
    int size;
};
typedef struct linkedlist LinkedList;

LinkedList * linkedList();

void addLast(LinkedList * theList, Node * nn);

void addFirst(LinkedList * theList, Node * nn);

void removeFirst(LinkedList * theList, void (*removeData)(void *));

void removeLast(LinkedList * theList, void (*removeData)(void *));

void removeItem(LinkedList * theList, Node * nn, void (*removeData)(void *), int (*compare)(const void *, const void *));

void clearList(LinkedList * theList, void (*removeData)(void *));

void printList(const LinkedList * theList, void (*convertData)(void *));

#endif // LINKEDLIST_H
