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
struct my_int
{
	int value;
};


/**
 * My style is to name my structure and then typedef it to something more usable
 */
typedef struct my_int MyInt;

/**
 * The buildInt method receives the input stream (in this case stdin)<br>
 * The method then:<br>
 * 1) Prompts the user to enter an integer<br>
 * 2) Reads that integer by using stream<br>
 * 3) Dynamically creates a 1 element structure using malloc/calloc<br>
 * 4) Stores the value previous entered inside the dynamically create<br>
 * 5) Returns the dynamically created structure<br>
 * 
 * @warning Please ensure you leave the input buffer empty
 */
void * buildInt(FILE * stream);

/**
 * The printInt method completes the following:<br>
 * 1) Receives a void pointer which it casts to a MyInt *<br>
 * 2) Prints to the screen Int - and the value extracted from the cast MyInt *
 */
void printInt(void * ptr);


/**
 * The cleanInt method completes the following:<br>
 * 1) Receives a void pointer which it casts to a MyInt *<br>
 * 2) Frees the cast MyInt *
 *
 * @warning All pointers need to be set to NULL after they are freed
 */
void cleanInt(void * ptr);


/**
 * @brief Compares two objects of type MyInt.
 *
 * MyInts are compared based on value
 *
 * @note The passed in items will need to be cast to the appropriate MyInt type.
 *
 * @param p1 - The void * representing an object of type MyInt
 * @param p2 - The void * representing an object of type MyInt
 * @return int - Representing order < 0 indicates p1 is less than p2,
 * > 0 indicates p1 is greater than p2 and == 0 indicates p1 == p2 for contents
 *
 * @warning - The passed in void * p1 is checked - exit(-99) if NULL
 * @warning - The passed in void * p2 is checked - exit(-99) if NULL
 */
int compareInt(const void * p1, const void * p2);


#endif
