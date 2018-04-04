#ifndef MYWORD_H
#define MYWORD_H

#include<stdio.h>
#include<stdlib.h>
#include<string.h>


struct my_word {
    char *word;
};

typedef struct my_word MyWord;

void *buildWord(FILE *stream);

void printWord(void *ptr);

void cleanWord(void *ptr);


#endif