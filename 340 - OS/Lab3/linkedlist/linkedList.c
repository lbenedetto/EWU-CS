#include "linkedList.h"

void deleteNode(Node *n, void (*removeData)(void *)) {
    n->prev->next = n->next;
    removeData(n);
    free(n);
}

void insertNode(Node *p, Node *new, Node *n) {
    new->prev = p;
    new->next = n;
    p->next = new;
    n->prev = new;
}

/**
 * @brief The so called "constructor" for the linked list
 *
 * The linked list constructor builds a non circular linked list that
 * contains a dummy head node.  This is meant to be similar to a Java
 * linked list constructor.
 *
 * @return LinkedList * - The linked list pointer that contains a dummy head node but is not circular.
 */
LinkedList *linkedList() {
    LinkedList *ll = calloc(1, sizeof(LinkedList));
    ll->size = 0;
    ll->head = calloc(1, sizeof(Node));
    return ll;
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
void addLast(LinkedList *theList, Node *nn) {
    if (theList == NULL || nn == NULL) exit(-99);
    Node *c = theList->head;
    for (int i = 0; i < theList->size; i++) {
        c = c->next;
    }
    c->next = nn;
    nn->prev = c;
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
void addFirst(LinkedList *theList, Node *nn) {
    if (theList == NULL || nn == NULL) exit(-99);
    insertNode(theList->head, nn, theList->head->next);
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
void removeFirst(LinkedList *theList, void (*removeData)(void *)) {
    if (theList == NULL) exit(-99);
    deleteNode(theList->head->next, removeData);
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
void removeLast(LinkedList *theList, void (*removeData)(void *)) {
    if (theList == NULL) exit(-99);
    Node *c = theList->head;
    for (int i = 0; i < theList->size; i++) {
        c = c->next;
    }
    deleteNode(c, removeData);
}


/**
 * @brief The remove item function for the linked list
 *
 * Removes the first occurrence of the specified element from this list, if it
 * is present.  If this list does not contain the element, it is unchanged.
 *
 * @param theList - The specified linked list
 * @param nn - The node to be removed
 * @param *removeData - The function pointer for freeing the specific data type
 * @param *compare - The compare function to compare specific data type
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 * @warning - The passed in Node * nn is checked - exit(-99) if NULL
 */
void removeItem(LinkedList *theList, Node *nn, void (*removeData)(void *), int (*compare)(const void *, const void *)) {
    if (theList == NULL || nn == NULL) exit(-99);
    Node *c = theList->head;
    for (int i = 0; i < theList->size; i++) {
        c = c->next;
        if (compare(c->data, nn->data) == 0) {
            deleteNode(c, removeData);
        }
    }
    theList->size = theList->size - 1;
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
void clearList(LinkedList *theList, void (*removeData)(void *)) {
    if (theList == NULL) return;
    Node *c = theList->head;
    for (int i = 0; i < theList->size; i++) {
        c = c->next;
        deleteNode(c, removeData);
    }
    theList->size = 0;
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
void printList(const LinkedList *theList, void (*convertData)(void *)) {
    if (theList == NULL) {
        printf("Empty List\n");
        return;
    }
    Node *c = theList->head;
    for (int i = 0; i < theList->size; i++) {
        c = c->next;
        convertData(c->data);
    }
}