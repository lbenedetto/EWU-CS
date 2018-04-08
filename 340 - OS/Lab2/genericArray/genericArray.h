/**
 * @file genericArray.h
 * A Generic Array file for holding a basic structure and the method headers that are written in a very
 * generic fashion
 */
#ifndef GENERICARRAY_H
#define GENERICARRAY_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/**
 * The name generic array structure that simply holds a void pointer<br>
 */
struct generic_array
{
	/**
	 * The void pointer for the data
	 */
	void * data;
};


/**
 * My style is to create a generic named structure and then typedef to a more usable name
 */
typedef struct generic_array GenericArray;


/**
 * The fillArray method, fills the array by reading a record from the file.
 *
 * @param in Representing the input stream
 * @param length The total number of elements in the array
 * @param function pointer for building the type
 * @return GenericArray * Representing the filled array
 */
GenericArray * fillArray(FILE * in, int length, void * (*buildType)(FILE * input));


/**
 * The printArray method.  The point of this method is to:<br>
 * The method loops thru each element of the array and simply calls the appropriate print method and prints the contents to the stream
 *
 * @param array Representing the GenericArray as a 1D pointer
 * @param length A pointer back to main for the length of the array
 * @param function pointer for print the contents of the void pointer -- note the function pointer is passed the void * data
 */
void printArray(GenericArray * array, int length, void (*printType)(void *));


/**
 * The cleanArray method.  The point of this method is to:<br>
 * The method loops thru each element of the array and simply calls the appropriate clean method and frees any dynamically allocated memory<br>
 * Each element of the appropriate type should be set to NULL if it is a pointer<br>
 * Once the for loop completes the array itself is freed.
 *
 * @param array Representing the GenericArray as a 1D pointer
 * @param length A pointer back to main for the length of the array
 * @param function pointer for cleaning the contents of the void pointer -- note the function pointer is passed the void * data
 */
void cleanArray(GenericArray * array, int length, void (*cleanType)(void *));


/**
 * The sortArray method uses the selectionSort to sort the array
 *
 * @param array Representing the GenericArray as a 1D pointer
 * @param length A pointer back to main for the length of the array
 * @param function pointer for comparing the data from the line if(compar(array[search], array[min]) < 0)
 */
void sortArray(GenericArray * array, int length, int (*compar)(const void * v1, const void * v2));


#endif
