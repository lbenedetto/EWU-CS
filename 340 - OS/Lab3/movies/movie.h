/**
 * @file movie.h
 * The data structure for the movie
 */

#ifndef MOVIE_H_
#define MOVIE_H_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "actor.h"
#include "../utils/myUtils.h"

/**
 * @brief The movie data structure.
 *
 * A movie contains a title, an array of actors, and an int for the number
 *
 * @note I prefer named structures and then the typedef after the structure
 */
struct movie
{
   Actor * actors;
   int totalActors;
   char * title;
};

typedef struct movie Movie;


/**
 * @brief Cleans up all dynamically allocated memory for the movie
 *
 * Cleans up and frees all the dynamically allocated memory 
 * In this case title, actors->first, actors->last and the actors array
 *
 * Each pointer in the specific data type is set to NULL after it has been freed.
 *
 * @param passedIn - The void * passed in representing the specific movie
 *
 * @warning - The passed in void * passedIn is checked - exit(-99) if NULL
 */
void cleanTypeMovie(void * ptr);


/**
 * @brief Builds and returns a single movie.
 *
 * Reads the initial data from the file and then builds
 * an object of type movie.
 *
 * @note The carriage return will be removed when reading from the file
 *
 * @param fin - The FILE * representing the open file
 * @return void * - Representing a movie object
 *
 * @warning - The passed in FILE * fin is checked - exit(-99) if NULL
 */
void * buildTypeMovie(FILE * fin);


/**
 * @brief Prints the specific movie.
 *
 * Format is title
 * Starting:
 * actor first actor last, actor first actor last
 *
 * @note: price is printed to two decimal places
 *
 * @param passedIn - The void * passed in representing a specific movie
 *
 * @warning - The passed in void * passedIn is checked - exit(-99) if NULL
 */
void printTypeMovie(void * passedIn);


/**
 * @brief Builds and returns a single movie.
 *
 * Reads the initial data from the keyboard by prompting the user.
 * A movie object is constructed.
 *
 * @note The carriage return will be removed when reading from the keyboard
 *
 * @param fin - The FILE * representing stdin
 * @return void * - Representing a movie object
 *
 * @warning - The passed in FILE * fin is checked - exit(-99) if NULL
 */
void * buildTypeMovie_Prompt(FILE * fin);


/**
 * @brief Compares two objects of type movie.
 *
 * movies are compared based on title and if the title are the same then totalActors
 *
 * @note The passed in items will need to be cast to the appropriate movie type.
 *
 * @param p1 - The void * representing an object of type movie
 * @param p2 - The void * representing an object of type movie
 * @return int - Representing order < 0 indicates p1 is less than p2,
 * > 0 indicates p1 is greater than p2 and == 0 indicates p1 == p2 for contents
 *
 * @warning - The passed in void * p1 is checked - exit(-99) if NULL
 * @warning - The passed in void * p2 is checked - exit(-99) if NULL
 */
int compareMovie(const void * p1, const void * p2);


#endif /* MOVIE_H_ */
