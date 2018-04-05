#ifndef MYINT_H
#define MYINT_H

#include<stdio.h>
#include<stdlib.h>

struct my_int {
    int value;
};
typedef struct my_int MyInt;

void *buildInt(FILE *stream);

void printInt(void *ptr);

void cleanInt(void *ptr);

#endif