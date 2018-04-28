#ifndef FILEUTIL_H
#define FILEUTIL_H

#include <stdio.h>
#include <stdlib.h>

#define MAX 100

/**
 * Checks if a file exists, works even if we don't have read permissions
 * @param filename
 * @return bool
 */
int doesFileExist(const char *filename);

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

/**
 * @brief Strinps the \r and \n from the string if they exist.
 *
 * Walks through the character array one character at a time
 * if a '\r' or '\n' are encountered it is changed to a '\0'
 * entered by the user is within range.
 *
 * @param The character array potentially containing a '\r' and/or '\n'
 *
 * @warning - int isFileEmpty(FILE *fp){The passed in pointer is checked - exit(-99) if NULL
 */
void strip(char *array);

char *concat(char *s1, char *s2);

int isFileEmpty(FILE *fp);

#endif // FILEUTIL_H
