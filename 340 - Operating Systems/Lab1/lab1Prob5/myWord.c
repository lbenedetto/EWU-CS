//
// Created by lars on 4/3/18.
//

#include "myWord.h"

void *buildWord(FILE *stream) {
    printf("Please enter word ");
    int c; //getchar() returns int
    int i = 0;
    char* asdfas = getline(stdin);
    char *chars = calloc(40, sizeof(char));
    while (((c = getchar()) != '\n') || (i >= 39)) {
        chars[i++] = c;
    }

    struct my_word myWord = {chars};
    struct my_word *p = &myWord;
    return (void *) p;
}

void printWord(void *ptr) {
    struct my_word *f;
    f = (struct my_word *) ptr;
    printf("Word: %s\n", f->word);
}

void cleanWord(void *ptr) {
    //free(ptr);
}