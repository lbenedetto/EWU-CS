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
struct movie {
	Actor *actors;
	int totalActors;
	char *title;
};

typedef struct movie Movie;

void cleanTypeMovie(void *ptr);

void *buildTypeMovie(FILE *fin);

void printTypeMovie(void *passedIn);

void *buildTypeMovie_Prompt(FILE *fin);

int compareMovie(const void *p1, const void *p2);

#endif /* MOVIE_H_ */
