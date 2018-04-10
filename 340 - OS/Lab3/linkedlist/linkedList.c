#include "linkedList.h"

/**
 * @brief The so called "constructor" for the linked list
 *
 * The linked list constructor builds a non circular linked list that
 * contains a dummy head node.  This is meant to be similar to a Java
 * linked list constructor.
 *
 * @return LinkedList * - The linked list pointer that contains a dummy head node but is not circular.
 */
LinkedList * linkedList(){

}


/**
 * @brief The add last function for the linked list
 *
 * Appends the specified node to the end of this list
 *
 * @param theList - The specified linked list
 * @param nn - The node to be added
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 * @warning - The passed in Node * nn is checked - exit(-99) if NULL
 */
void addLast(LinkedList * theList, Node * nn){

}


/**
 * @brief The add first function for the linked list
 *
 * Appends the specified node to the beginning of this list
 *
 * @param theList - The specified linked list
 * @param nn - The node to be added
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 * @warning - The passed in Node * nn is checked - exit(-99) if NULL
 */
void addFirst(LinkedList * theList, Node * nn){

}


/**
 * @brief The remove first function for the linked list
 *
 * Removes the specified node to the beginning of this list
 *
 * @param theList - The specified linked list
 * @param *removeData - The function pointer for freeing the specific data type
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 */
void removeFirst(LinkedList * theList, void (*removeData)(void *)){

}


/**
 * @brief The remove last function for the linked list
 *
 * Removes the specified node to the end of this list
 *
 * @param theList - The specified linked list
 * @param *removeData - The function pointer for freeing the specific data type
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 */
void removeLast(LinkedList * theList, void (*removeData)(void *)){

}


/**
 * @brief The remove item function for the linked list
 *
 * Removes the first occurrence of the specified element from this list, if it
 * is present.  If this list does not contain the element, it is unchanged.
 *
 * @param theList - The specified linked list
 * @param nn - The node to be added
 * @param *removeData - The function pointer for freeing the specific data type
 * @param *compare - The compare function to compare specific data type
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 * @warning - The passed in Node * nn is checked - exit(-99) if NULL
 */
void removeItem(LinkedList * theList, Node * nn, void (*removeData)(void *), int (*compare)(const void *, const void *)){

}


/**
 * @brief Empties the list and its contents
 *
 * Removes all of the elements from this list.  The list will be empty after the function completes
 *
 * @param theList - The specified linked list
 * @param *removeData - The function pointer for freeing the specific data type
 * @param *compare - The compare function to compare specific data type
 *
 * @warning - The passed in LinkedList * theList is checked - if NULL nothing happens
 */
void clearList(LinkedList * theList, void (*removeData)(void *)){

}


/**
 * @brief Prints the contents of this linked list
 *
 * Prints the contents, if there are any of this linked list.  If the list is NULL
 * or empty, "Empty List" is printed.
 *
 * @param theList - The specified linked list
 * @param *convert - The function pointer for printing the specific data type
 *
 * @warning - The passed in LinkedList * theList is checked - if NULL "Empty List" is printed
 */
void printList(const LinkedList * theList, void (*convertData)(void *)){

}