#include <stdio.h>
#include <string.h>
#include "stdlib.h"
#include <stdio.h>
#include "fileUtils.h"

/**
* A function that asks the user for an input file name, tries to open that file, and ensures
* that file is open before the file pointer is returned.
*
* @return FILE * to the open input file
*/
FILE *openInputFile_Prompt() {
    FILE *fp = NULL;
    do {
        printf("Please enter filename: ");
        char str[100];
        int i;
        fgets(str, 100, stdin);

        i = strlen(str) - 1;
        if (str[i] == '\n')
            str[i] = '\0';

        fp = fopen(str, "r");
    } while (fp == NULL);
    return fp;
}


/**
 * A function that runs through the file line by line counting each line
 *
 * @param fin Representing the file pointer to an open file
 * @param linesPer Representing the number of lines read from the file for 1 record. Example linesPer -> 3 means 3 lines make up a single record
 * @return int Representing the number of records in the file
 *
 * @throws exit(-99) If the FILE * is null or linesPer is less than 1 -- print an error and exit(-99)
 * @throws exit(-99) If count == 0 or count % linesPer != 0
 *
 * @warning You must reset/rewind the file pointer before the function exits
 */
int countRecords(FILE *fin, int linesPer) {
    if (fin == NULL) {
        exit(-99);
    }
    int lines = 0;
    char ch;

    fseek(fin, 0, SEEK_SET);

    while (EOF != (ch = (char) fgetc(fin)))
        if (ch == '\n')
            lines++;

    fseek(fin, 0, SEEK_SET);

    if (lines == 0 || lines % linesPer != 0) exit(-99);
    return lines / linesPer;
}