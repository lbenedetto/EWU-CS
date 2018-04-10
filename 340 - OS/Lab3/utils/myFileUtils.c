#include <stdlib.h>
#include "myFileUtils.h"

char *readLine(FILE *fin) {
    char *str = NULL;
    size_t size;
    ssize_t chars = getline(&str, &size, fin);
    if (str[chars - 1] == '\n') {
        str[chars - 1] = '\0';
    }
    return str;
}

void readInt(FILE * fin, int* i){
    char *temp = readLine(fin);
    sscanf(temp, "%d", i);
    free(temp);
}
