#ifndef PIPES_H
#define PIPES_H

#define _GNU_SOURCE

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>
#include "../tokenize/makeArgs.h"

void pipeIt(char *PATH, char *s, int commandCount);


#endif 
