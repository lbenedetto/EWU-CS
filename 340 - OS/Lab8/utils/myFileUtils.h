#ifndef LAB3_MYFILEUTILS_H
#define LAB3_MYFILEUTILS_H

#include <stdio.h>

/**
 * Reads a single line of input from specified input
 * @param fin input file
 * @return string
 */
char *readLine(FILE *fin);

/**
 * Reads a line and converts it to an int
 * @param fin input file
 * @param i address to store int in
 * @return
 */
void readInt(FILE *fin, int *i);


#endif //LAB3_MYFILEUTILS_H
