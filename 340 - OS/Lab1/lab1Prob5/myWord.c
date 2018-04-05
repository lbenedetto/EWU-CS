#include "myWord.h"

void *buildWord(FILE *stream) {
    printf("Please enter word ");
    char buf[250];
    fgets(buf, 250, stdin);
    char *str = calloc(strlen(buf) + 1, sizeof(char));
    strncpy(str, buf, strlen(buf) + 1);
    MyWord *myWord = calloc(1, sizeof *myWord);
    myWord->word = str;
    return (void *) myWord;
}

void printWord(void *ptr) {
    char *str = ((struct my_word *) ptr)->word;
    printf("Word: %s", str);
}

void cleanWord(void *ptr) {
    MyWord *myWord = (MyWord *) ptr;
    free(myWord->word);
    free(myWord);
}