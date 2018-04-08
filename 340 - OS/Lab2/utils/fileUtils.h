/**
 * @file fileUtils.h
 * Basic fileUtils needed for lab 2
 */
#ifndef FILEUTILS_H
#define FILEUTILS_H

#include <stdio.h>
#include <stdlib.h>

/** 
 * A constant that represents no string will be greater than this number
 */
#define MAX 100


/** 
 * A function that asks the user for an input file name, tries to open that file, and ensures
 * that file is open before the file pointer is returned.
 *
 * @return FILE * to the open input file
 */
FILE * openInputFile_Prompt();


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
int countRecords(FILE * fin, int linesPer);

#endif // FILEUTILS_H

