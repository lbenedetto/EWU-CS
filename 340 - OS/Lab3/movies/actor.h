/**
 * @file actor.h
 * The data structure to hold the actor
 */


#ifndef ACTOR_H_
#define ACTOR_H_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/**
 * @brief The actor data structure.
 *
 * A actor contains a first name and last name of the actor.
 *
 * @note I prefer named structures and then the typedef after the structure
 */
struct actor
{
	char *first;
	char *last;
};

typedef struct actor Actor;

#endif /* ACTOR_H_ */
