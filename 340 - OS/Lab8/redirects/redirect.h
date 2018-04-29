#ifndef LAB8_REDIRECT_H
#define LAB8_REDIRECT_H

#define _GNU_SOURCE

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/stat.h>
#include "stdbool.h"
#include <unistd.h>
#include <fcntl.h>

#define std_in 0
#define std_out 1
#define child 0

bool checkRedirects(char *s, int *inCount, int *outCount);

void redirectIt(char *PATH, char *s, bool isIn, bool isOut);

void fileToCommandToFile(char *PATH, char *s);

void fileToCommand(char *PATH, char *s);

void commandToFile(char *PATH, char *s);

#endif //LAB8_REDIRECT_H
