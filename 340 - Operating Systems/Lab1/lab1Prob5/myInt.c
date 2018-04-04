//
// Created by lars on 4/3/18.
//

#include "myInt.h"

void *buildInt(FILE *stream){
    int i;
    printf("Please enter an integer ");
    fscanf(stream, "%d", &i);
    struct my_int myInt = {i};
    struct my_int *p = &myInt;
    return (void *) p;
}

void printInt(void *ptr){
    int i = ((struct my_int *)ptr)->value;
    printf("Int: %d\n", i);
}

void cleanInt(void *ptr){
    //free(ptr);
}