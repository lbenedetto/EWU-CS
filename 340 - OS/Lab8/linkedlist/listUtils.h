/**
 * @file listUtils.h
 * The purpose of this file is to outline functions to help build a basic
 * doubly linked list.  The presumption is the information will be read
 * from a file.  A node of the specific data will be built and be added
 * to the list usually by calling the addFirst method.
 *
 * @note This file will never be changed
 */

#ifndef LISTUTILS_H
#define LISTUTILS_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "linkedList.h"

#define MAX 100

Node *buildNode(FILE *fin, void *(*buildData)(FILE *in));

Node *buildNode_Type(void *passedIn);

void sort(LinkedList *theList, int (*compare)(const void *, const void *));

void buildListTotal(LinkedList *myList, int total, FILE *fin, void *(*buildData)(FILE *in));

#endif // LISTUTILS_H
