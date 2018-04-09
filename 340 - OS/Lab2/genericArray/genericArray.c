#include <stdlib.h>
#include "genericArray.h"
#include "../stock/stock.h"
/**
 * The fillArray method, fills the array by reading a record from the file.
 *
 * @param in Representing the input stream
 * @param length The total number of elements in the array
 * @param function pointer for building the type
 * @return GenericArray * Representing the filled array
 */
GenericArray *fillArray(FILE *in, int length, void *(*buildType)(FILE *input)) {
    GenericArray *array = calloc(length, sizeof(GenericArray));
    for(int i = 0; i < length; i++){
        array[i].data = buildType(in);
    }
    return array;
}


/**
 * The printArray method.  The point of this method is to:<br>
 * The method loops thru each element of the array and simply calls the appropriate print method and prints the contents to the stream
 *
 * @param array Representing the GenericArray as a 1D pointer
 * @param length A pointer back to main for the length of the array
 * @param function pointer for print the contents of the void pointer -- note the function pointer is passed the void * data
 */
void printArray(GenericArray * array, int length, void (*printType)(void *)){
    for(int i = 0; i < length; i++){
        printType(array[i].data);
    }
}


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
void cleanArray(GenericArray *array, int length, void (*cleanType)(void *)) {
    for (int i = 0; i < length; i++) {
        cleanType(array[i].data);
    }
    free(array);
}


/**
 * The sortArray method uses the selectionSort to sort the array
 *
 * @param array Representing the GenericArray as a 1D pointer
 * @param length A pointer back to main for the length of the array
 * @param function pointer for comparing the data from the line if(compar(array[search], array[min]) < 0)
 */
void sortArray(GenericArray *array, int length, int (*compar)(const void *v1, const void *v2)) {
//    for (int i = 0; i < length; i++) {
//        int min = 0;
//        for (int search = i; search < length; search++) {
//            if (compar((void *) &array[search], (void *) &array[min]) < 0) {
//                GenericArray temp = array[min];
//                array[min] = array[search];
//                array[search] = array[min];
//                min = search;
//            }
//        }
//    }
}