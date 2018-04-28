#include "listUtils.h"
#include "../utils/utils.h"

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
Node *buildNode(char *passedIn, int copy) {
	Node *node = calloc(1, sizeof(Node));
	if (copy) {
		node->data = calloc(strlen(passedIn) + 1, sizeof(char));
		strcpy(node->data, passedIn);
	} else node->data = passedIn;
	return node;
}

void swap(Node *n1p, Node *n1, Node *n2, Node *n2n) {
	//Ignore previous connections and just build new ones
	n1p->next = n2;//n1p ->n2
	n2->prev = n1p;//n1p<->n2

	n2->next = n1;//n1p<->n2 ->n1
	n1->prev = n2;//n1p<->n2<->n1

	n1->next = n2n;//n1p<->n2<->n1 ->n2n
	if (n2n != NULL) n2n->prev = n1;//n1p<->n2<->n1<->n2n
}

/**
 * @brief Sorts the linked list.
 *
 * The sort function resides here because sorting a linked list
 * is not considered an integral function within the required
 * linked list functions.
 *
 * @param theList - The linked list  * representing the list
 *
 * @warning - The passed in LinkedList * theList is checked - exit(-99) if NULL
 * @warning - The theList-> size is checked and if the list contains 0 or 1 element then the function
 * does not attempt to sort the list.
 */
void sort(LinkedList *theList) {
	if (theList == NULL) exit(-99);
	if (theList->size < 2) return;

	Node *c = theList->head->next;
	int swapped;
	do {
		swapped = 0;
		while (c != NULL && c->next != NULL) {
			if (strcmp(c->data, c->next->data) > 0) {
				swap(c->prev, c, c->next, c->next->next);
				swapped = 1;
			}
			c = c->next;
		}
		c = theList->head->next;
	} while (swapped == 1);
}