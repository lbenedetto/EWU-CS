#include <stdio.h>
#include <string.h>
/**
* A function that asks the user for an input file name, tries to open that file, and ensures
* that file is open before the file pointer is returned.
*
* @return FILE * to the open input file
*/
FILE *openInputFile_Prompt() {
    printf("Please enter filename: ");
    char buf[250];
    fgets(buf, 250, stdin);
    char *str = calloc(strlen(buf) + 1, sizeof(char));
    strncpy(str, buf, strlen(buf) + 1);
    File *fp;
    fp = fopen(str, "r");
    if(fp == NULL){
        printf("Could not open file");
    }
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
    if(fin == NULL) exit(-99);
    int count = 0;

    //Read the entire file into a string
    fseek(fin, 0, SEEK_END);
    long fsize = ftell(f);
    fseek(fin, 0, SEEK_SET);
    char *str = calloc(fsize, sizeof(char));
    fread(string, sizeof(char), fsize, fin);
    fseek(fin, 0, SEEK_SET);

    //Count the number of newlines
    for(int i = 0; i < fsize; i++){
        if(str[i] == '\n') count++;
    }

    if(count == 0 || count % linesPer != 0) exit(-99);
    return count % linesPer;
}