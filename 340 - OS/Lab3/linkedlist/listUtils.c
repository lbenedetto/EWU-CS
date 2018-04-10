#include "listUtils.h"
#include "../utils/fileUtils.h"

/**
 * @brief Builds a node that contains a pointer to the specific data type.
 *
 * The buildNode function builds a node for the linked list. The node's void *
 * data will refer to the specific data type. The specific data type is built
 * by calling the appropriate specific data function, which is called via
 * function pointer. FYI I do an addFirst in my build
 *
 * @param fin - The FILE * used to read the data from the file
 * @param *buildData - The function pointer to build an object of the specific data type
 * @return Node * - Representing a node for the linked list containing the specific data type.
 *
 * @note - The first parameter FILE * fin is not used in the function.  It is entirely
 * used as a pass through parameter for the function pointer which is passed a FILE * also.
 *
 * @warning - Since FILE *fin is a pass through it is not checked.
 */
Node *buildNode(FILE *fin, void *(*buildData)(FILE *in)) {
    Node *node = calloc(1, sizeof(Node));
    node->data = buildData(fin);
    return node;
}


/**
 * @brief Builds a node that contains a call to a specific type.
 *
 * The buildNode_Type function builds a node for the linked list. The node's void
 * data will refer to the specific data type. The specific data type is built
 * by calling the appropriate specific data function, which is called via
 * function pointer.
 *
 * @param *passedIn - The void * for the data type being created
 * @return Node * - Representing a node for the linked list containing the specific data type.
 */
Node *buildNode_Type(void *passedIn) {
    Node *node = calloc(1, sizeof(Node));
    node->data = passedIn;
    return node;
}

void swap(Node *n1p, Node *n1, Node *n2, Node *n2n) {
    //Ignore previous connections and just build new ones
    n1p->next = n2;//n1p ->n2
    n2->prev = n1p;//n1p<->n2

    n2->next = n1;//n1p<->n2 ->n1
    n1->prev = n2;//n1p<->n2<->n1

    n1->next = n2n;//n1p<->n2<->n1 ->n2n
    if(n2n != NULL) n2n->prev = n1;//n1p<->n2<->n1<->n2n
}

/**
 * @brief Sorts the linked list.
 *
 * The sort function resides here because sorting a linked list
 * is not considered an integral function within the required
 * linked list functions.
 *
 * @param theList - The linked list  * representing the list
 * @param *compare - The specific data type compare function as a function pointer
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 * @warning - The theList-> size is checked and if the list contains 0 or 1 element then the function
 * does not attempt to sort the list.
 *
 * @note uses TreeSort
 */
void sort(LinkedList *theList, int (*compare)(const void *, const void *)) {
    if (theList == NULL) exit(-99);
    if (theList->size < 2) return;

    Node *c = theList->head->next;
    int swapped;
    do {
        swapped = 0;
        while (c != NULL && c->next != NULL) {
            if (compare(c->data, c->next->data) > 0) {
                swap(c->prev, c, c->next, c->next->next);
                swapped = 1;
            }
            c = c->next;
        }
        c = theList->head->next;
    } while (swapped == 1);
}


/**
 * @brief Builds an initial linked list.
 *
 * The build list creates an initial linked list by reading from
 * the file, calling the buildNode function and then adds that node
 * into the list by calling addFirst.
 *
 * @param theList - The linked list  * representing the list
 * @param total - The total items to be placed into the list
 * @param fin - The FILE * used to read the data from the file
 * @param *buildData - The function pointer to call the specific function
 * to build the appropriate data type.
 *
 * @note - The parameter FILE * fin is not used in the function.  It is entirely
 * used as a pass through parameter for the function pointer which is also passed a FILE *.
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 * @warning - The passed in int total is checked - exit(-99) if <= 0
 * @warning - Since FILE *fin is a pass through it is not checked.
 */
void buildListTotal(LinkedList *myList, int total, FILE *fin, void *(*buildData)(FILE *in)) {
    if (myList == NULL || total <= 0) exit(-99);
    for (int i = 0; i < total; i++) {
        Node *n = calloc(1, sizeof(Node));
        n->data = buildData(fin);
        addFirst(myList, n);
    }
}
