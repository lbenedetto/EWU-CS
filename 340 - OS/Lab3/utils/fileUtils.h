/**
 * @file fileUtils.h
 * The fileUtil.h defines the basic file utility functions that will be
 * required during the course of the quarter.
 */

#ifndef FILEUTIL_H
#define FILEUTIL_H

#include <stdio.h>
#include <stdlib.h>

#define MAX 100


/**
 * @brief Prompts the user for the name of the input file and attempts to open that file.
 *
 * Prompts the user for the name of the input file
 * Trys to open the file, if the file is opened the FILE * is returned
 * If the file does not open, the user is reprompted.
 *
 * @note The carriage return is removed from the input buffer
 *
 * @return FILE * - Representing the open file
 */
FILE * openInputFile_Prompt();

/**
 * @brief Prompts the user for the name of the supplied type for the input file and attempts to open that file.
 *
 * Prompts the user for the name of the input file
 * Trys to open the file, if the file is opened the FILE * is returned
 * If the file does not open, the user is reprompted.
 *
 * @note The carriage return is removed from the input buffer
 *
 * @param type - The char * for the type
 * @return FILE * - Representing the open file
 */
FILE * openInputFileType_Prompt(char * type);

/**
 * @brief Counts the number of records that are in the file.
 *
 * Counts the number of lines in the file and then divides by the 
 * number of lines per record. 
 *
 * @note The FILE * is rewound at the end of the function
 * 
 * @param fin - The FILE * representing the open file
 * @param linesPerRecord - Representing the number of lines in the file that make up a record
 * @return int - Representing the number of records in the file
 *
 * @warning - The passed in FILE * fin is checked - exit(-99) if NULL
 * @warning - The passed in int linesPerRecord is checked - exit(-99) if <= 0
 * @warning - If count is 0 the program is ended with a -99 status
 */
int countRecords(FILE * fin, int linesPerRecord);

/**
 * @brief Reads the number of records from the file
 *
 * The number of records must be indicated in the file.  This
 * will read that value from the file.
 *
 * @note The carriage return is removed from the input buffer
 *
 * @param fin - The FILE * representing the open file
 * @return int - Representing the number of records in the file
 * 
 * @warning - The passed in FILE * fin is checked - exit(-99) if NULL
 * @warning - The number of records read from the file is checked - exit(-99) if <= 0
 */
int readTotal(FILE * fin);

#endif // FILEUTIL_H
