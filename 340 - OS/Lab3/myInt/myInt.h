/**
 * @file myInt.h
 * A simple integer structure that holds a single int type called value
 * generic fashion
 */

#ifndef MYINT_H
#define MYINT_H

#include<stdio.h>
#include<stdlib.h>


/**
 * The named structure my_int that holds a single integer named value
*/
struct my_int {
	int value;
};


/**
 * My style is to name my structure and then typedef it to something more usable
 */
typedef struct my_int MyInt;

void *buildInt(FILE *stream);

void printInt(void *ptr);

void cleanInt(void *ptr);

int compareInt(const void *p1, const void *p2);

#endif
