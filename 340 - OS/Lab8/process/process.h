#ifndef PROCESS_H
#define PROCESS_H

#define _GNU_SOURCE

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>

void forkIt(char *PATH[], char **argv);

#endif
