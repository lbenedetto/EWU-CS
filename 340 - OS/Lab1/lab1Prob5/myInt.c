#include "myInt.h"

void *buildInt(FILE *stream) {
    int i;
    printf("Please enter an integer ");
    fscanf(stream, "%d", &i);
    MyInt *myInt = calloc(1, sizeof *myInt);
    myInt->value = i;
    return (void *) myInt;
}

void printInt(void *ptr) {
    int i = ((struct my_int *) ptr)->value;
    printf("Int: %d\n", i);
}

void cleanInt(void *ptr) {
    free((MyInt *) ptr);
}