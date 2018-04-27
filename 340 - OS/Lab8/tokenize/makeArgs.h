#ifndef MAKEARGS_H
#define MAKEARGS_H


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../utils/myUtils.h"

#define MAX 100

int countTokens(char *s, const char *t);

void clean(int argc, char **argv);

void printargs(int argc, char **argv);

int makeargss(char *s, char ***argv, char *t, int size);

int makeargs(char *s, char ***argv, char *t);

#endif
