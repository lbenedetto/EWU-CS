#ifndef MAKEARGS_H
#define MAKEARGS_H


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../utils/myUtils.h"

#define MAX 100


void clean(int argc, char **argv);
void printargs(int argc, char **argv);
int makeargs(char *s, char *** argv);

#endif
