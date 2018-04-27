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

Node *buildNode(char *passedIn);

void sort(LinkedList *theList);

#endif // LISTUTILS_H
